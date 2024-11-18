package ru.sadykov.socialnetwork.friend.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.sadykov.socialnetwork.friend.client.AuthClient;
import ru.sadykov.socialnetwork.friend.entity.Friend;
import ru.sadykov.socialnetwork.friend.entity.Status;
import ru.sadykov.socialnetwork.friend.event.FriendAddEvent;
import ru.sadykov.socialnetwork.friend.exception.InvalidRequestParameterException;
import ru.sadykov.socialnetwork.friend.exception.UserNotFoundException;
import ru.sadykov.socialnetwork.friend.exception.localization.LocalizationExceptionMessage;
import ru.sadykov.socialnetwork.friend.factory.FriendFactory;
import ru.sadykov.socialnetwork.friend.finder.FriendFinder;
import ru.sadykov.socialnetwork.friend.handler.ResponseMessageHandler;
import ru.sadykov.socialnetwork.friend.repository.FriendRepository;
import ru.sadykov.socialnetwork.friend.responsemessage.localization.LocalizationResponseMessage;
import ru.sadykov.socialnetwork.friend.service.dto.FriendResponseDto;
import ru.sadykov.socialnetwork.friend.updater.FriendUpdater;

import java.util.Optional;

@Slf4j
@Service
@RequiredArgsConstructor
public class FriendServiceImpl implements FriendService {

    private final FriendFinder friendFinder;
    private final FriendFactory friendFactory;
    private final FriendRepository friendRepository;
    private final ResponseMessageHandler responseMessageHandler;
    private final FriendUpdater friendUpdater;
    private final KafkaTemplate<String, FriendAddEvent> kafkaTemplate;

    private final LocalizationExceptionMessage localizationExceptionMessage;
    private final LocalizationResponseMessage localizationResponseMessage;

    private final AuthClient authClient;

    @Override
    @Transactional
    public FriendResponseDto addFriend(long userId, long subscriberId) {

        boolean userExists = authClient.userIsExists(subscriberId);

        if (userId == subscriberId) {
            throw new InvalidRequestParameterException(localizationExceptionMessage.getAddYourselfExc());
        }

        if (!userExists) {
            throw new UserNotFoundException(String.format(localizationExceptionMessage.getUserNotFound(), subscriberId));
        }

        String message;

        Optional<Friend> friendOptional = friendFinder.findFriend(userId, subscriberId);

        if (friendOptional.isEmpty()) {
            FriendFactory.FollowerRequest followerRequest = FriendFactory.FollowerRequest.builder()
                    .userId(subscriberId)
                    .subscriberId(userId)
                    .status(Status.SUBSCRIBER)
                    .build();

            Friend friend = friendFactory.create(followerRequest);

            friendRepository.save(friend);

            message = localizationResponseMessage.getYouAreSub();

        } else {
            Friend friend = friendOptional.get();

            Optional<String> responseResult = responseMessageHandler.handle(friend, subscriberId);

            message = responseResult.orElseGet(() -> {
                Friend update = friendUpdater.update(friend, Status.FRIEND);

                FriendAddEvent addFriendEvent = new FriendAddEvent(update.getSubscriberId(), "You add friend");

                log.info("Start - Sending AddFriendEvent {} to Kafka topic friendship-notification", addFriendEvent);
                kafkaTemplate.send("friendship-notification", addFriendEvent);
                log.info("End - Sending AddFriendEvent {} to Kafka topic friendship-notification", addFriendEvent);

                return localizationResponseMessage.getAreYouFriend();
            });
        }

        return new FriendResponseDto(message);
    }
}
