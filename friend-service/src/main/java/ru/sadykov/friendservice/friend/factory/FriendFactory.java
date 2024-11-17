package ru.sadykov.friendservice.friend.factory;

import lombok.Builder;
import lombok.Getter;
import org.springframework.stereotype.Component;
import ru.sadykov.friendservice.friend.entity.Friend;
import ru.sadykov.friendservice.friend.entity.Status;

@Component
public class FriendFactory {

    public Friend create(FollowerRequest followerRequest) {
        Friend friend = new Friend();
        friend.setUserId(followerRequest.getUserId());
        friend.setSubscriberId(followerRequest.getSubscriberId());
        friend.setStatus(followerRequest.getStatus());

        return friend;
    }

    @Getter
    @Builder
    public static class FollowerRequest {
        private long userId;
        private long subscriberId;
        private Status status;
    }
}


