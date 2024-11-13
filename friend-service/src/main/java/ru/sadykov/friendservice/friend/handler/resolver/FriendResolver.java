package ru.sadykov.friendservice.friend.handler.resolver;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import ru.sadykov.friendservice.friend.entity.Friend;
import ru.sadykov.friendservice.friend.entity.Status;
import ru.sadykov.friendservice.friend.responsemessage.localization.LocalizationResponseMessage;
import ru.sadykov.friendservice.friend.responsemessage.localization.ResponseMessageSource;

import java.util.Optional;

@Component
@RequiredArgsConstructor
public class FriendResolver implements ResponseMessageResolver {

    private final LocalizationResponseMessage localizationResponseMessage;

    @Override
    public Optional<String> resolve(Friend friend, long subscriberId) {
        Optional<String> msg = Optional.empty();

        if (Status.FRIEND.equals(friend.getStatus())) {
            msg = Optional.of(localizationResponseMessage.getAlreadyFriend());
        }

        return msg;
    }
}
