package ru.sadykov.socialnetwork.friend.updater;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import ru.sadykov.socialnetwork.friend.entity.Friend;
import ru.sadykov.socialnetwork.friend.entity.Status;
import ru.sadykov.socialnetwork.friend.repository.FriendRepository;

@Component
@RequiredArgsConstructor
public class FriendUpdater {

    private final FriendRepository friendRepository;


    public Friend update(Friend friend, Status status) {
        friend.setStatus(status);

        return friendRepository.save(friend);
    }
}
