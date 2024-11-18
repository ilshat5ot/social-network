package ru.sadykov.socialnetwork.friend.handler.resolver;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import ru.sadykov.socialnetwork.friend.entity.Friend;
import ru.sadykov.socialnetwork.friend.entity.Status;
import ru.sadykov.socialnetwork.friend.responsemessage.localization.LocalizationResponseMessage;

import java.util.Optional;

@Component
@RequiredArgsConstructor
public class SubscribeResolver implements ResponseMessageResolver {

    private final LocalizationResponseMessage localizationResponseMessage;

    @Override
    public Optional<String> resolve(Friend friend, long subscriberId) {

        Optional<String> msg = Optional.empty();

        if (Status.SUBSCRIBER.equals(friend.getStatus()) && friend.getUserId().equals(subscriberId)) {
            msg = Optional.of(localizationResponseMessage.getRequestAlreadySent());
        }

        return msg;
    }
}
