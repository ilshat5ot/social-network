package ru.sadykov.friendservice.friend.updater;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import ru.sadykov.friendservice.friend.entity.Friend;
import ru.sadykov.friendservice.friend.entity.Status;
import ru.sadykov.friendservice.friend.repository.FriendRepository;

@Component
@RequiredArgsConstructor
public class FriendUpdater {

    private final FriendRepository friendRepository;


    public Friend update(Friend friend, Status status) {
        friend.setStatus(status);

        return friendRepository.save(friend);
    }
}
