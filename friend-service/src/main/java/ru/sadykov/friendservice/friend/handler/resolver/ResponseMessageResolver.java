package ru.sadykov.friendservice.friend.handler.resolver;

import ru.sadykov.friendservice.friend.entity.Friend;

import java.util.Optional;

public interface ResponseMessageResolver {

    Optional<String> resolve(Friend friend, long subscriberId);
}
