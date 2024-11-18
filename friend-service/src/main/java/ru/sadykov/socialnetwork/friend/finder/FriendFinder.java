package ru.sadykov.socialnetwork.friend.finder;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import ru.sadykov.socialnetwork.friend.entity.Friend;
import ru.sadykov.socialnetwork.friend.entity.QFriend;
import ru.sadykov.socialnetwork.friend.repository.FriendRepository;

import java.util.Optional;

@Component
@RequiredArgsConstructor
public class FriendFinder {

    private final FriendRepository friendRepository;

    public Optional<Friend> findFriend(long userId, long subscriberId) {
        QFriend qFriend = QFriend.friend;

        return friendRepository.findOne(
                qFriend.userId.eq(userId).and(qFriend.subscriberId.eq(subscriberId))
                        .or(qFriend.userId.eq(subscriberId).and(qFriend.subscriberId.eq(userId)))
                        .and(qFriend.isDeleted.eq(false))
        );
    }
}
