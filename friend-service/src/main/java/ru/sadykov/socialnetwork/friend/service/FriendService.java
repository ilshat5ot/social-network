package ru.sadykov.socialnetwork.friend.service;

import ru.sadykov.socialnetwork.friend.service.dto.FriendResponseDto;

public interface FriendService {

    FriendResponseDto addFriend(long userId, long subscriberId);
}
