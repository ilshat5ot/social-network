package ru.sadykov.friendservice.friend.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import ru.sadykov.friendservice.friend.entity.Friend;
import ru.sadykov.friendservice.friend.entity.Status;
import ru.sadykov.friendservice.friend.exception.InvalidRequestParameterException;
import ru.sadykov.friendservice.friend.exception.localization.LocalizationExceptionMessage;
import ru.sadykov.friendservice.friend.factory.FriendFactory;
import ru.sadykov.friendservice.friend.finder.FriendFinder;
import ru.sadykov.friendservice.friend.handler.ResponseMessageHandler;
import ru.sadykov.friendservice.friend.responsemessage.localization.LocalizationResponseMessage;
import ru.sadykov.friendservice.friend.repository.FriendRepository;

import java.util.Optional;

@Service
@RequiredArgsConstructor
public class FriendServiceImpl implements FriendService {

    private final FriendFinder friendFinder;
    private final FriendFactory friendFactory;
    private final FriendRepository friendRepository;
    private final ResponseMessageHandler responseMessageHandler;

    private final LocalizationExceptionMessage localizationExceptionMessage;
    private final LocalizationResponseMessage localizationResponseMessage;

    @Override
    public String addFriend(long userId, long subscriberId) {

        // TODO check exist user
        String message;

        if (userId == subscriberId) {
            throw new InvalidRequestParameterException(localizationExceptionMessage.getAddYourselfExc());
        }

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

            message = responseResult.orElseGet(() -> saveFriend(friend));
        }

        return message;
    }

    private String saveFriend(Friend friend) {
        friend.setStatus(Status.FRIEND);
        friendRepository.save(friend);
        return localizationResponseMessage.getAreYouFriend();
    }
}

// проверить существует ли пользователь запрос на аус сервис
// найти запись по юзерИд, фолловерИд, или фолловерИд, юзерИд, не удалена
//Если записи нет, то создаем ее текущий юзерИд, фолловерИд, статус - фолловер
//Если запись уже существует, то проверяем статус записи
// Если уже друзья, отправляем сообщение, что вы уже друзья
// Если юзерИд из пути совпадает с юзерИд в сущности, отправляем запись вы уже отправили заявку или вы подписаны на пользователя
// Если юзерИд из пути не совпадает с юзерИд из сущности, отправить запись вы приняли пользователя в друзья

