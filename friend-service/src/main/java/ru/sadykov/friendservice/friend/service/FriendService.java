package ru.sadykov.friendservice.friend.service;

import ru.sadykov.friendservice.friend.service.dto.FriendResponseDto;

public interface FriendService {

    FriendResponseDto addFriend(long userId, long subscriberId);
}
