package ru.sadykov.socialnetwork.friend.handler.resolver;

import ru.sadykov.socialnetwork.friend.entity.Friend;

import java.util.Optional;

public interface ResponseMessageResolver {

    Optional<String> resolve(Friend friend, long subscriberId);
}
