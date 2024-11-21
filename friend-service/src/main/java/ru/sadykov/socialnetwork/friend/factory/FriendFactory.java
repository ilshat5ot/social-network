package ru.sadykov.socialnetwork.friend.factory;

import lombok.Builder;
import lombok.Getter;
import org.springframework.stereotype.Component;
import ru.sadykov.socialnetwork.friend.entity.Friend;
import ru.sadykov.socialnetwork.friend.entity.Status;

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


