package ru.sadykov.friendservice.friend.handler;

import lombok.NoArgsConstructor;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import ru.sadykov.friendservice.friend.entity.Friend;
import ru.sadykov.friendservice.friend.handler.resolver.ResponseMessageResolver;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Component
@RequiredArgsConstructor
public class ResponseMessageHandler {

    private final List<ResponseMessageResolver> responseMessageResolvers;

    public Optional<String> handle(Friend friend, long subscriberId) {

        Optional<String> msg = Optional.empty();

        for (ResponseMessageResolver resolver : responseMessageResolvers) {
            Optional<String> message = resolver.resolve(friend, subscriberId);

            if (message.isPresent()) {
                msg = message;
            }
        }

        return msg;
    }
}
