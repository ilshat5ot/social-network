package ru.sadykov.friendservice.friend.service;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Spy;
import org.mockito.junit.jupiter.MockitoExtension;
import ru.sadykov.friendservice.friend.entity.Friend;
import ru.sadykov.friendservice.friend.entity.Status;
import ru.sadykov.friendservice.friend.exception.InvalidRequestParameterException;
import ru.sadykov.friendservice.friend.exception.localization.LocalizationExceptionMessage;
import ru.sadykov.friendservice.friend.factory.FriendFactory;
import ru.sadykov.friendservice.friend.finder.FriendFinder;
import ru.sadykov.friendservice.friend.handler.ResponseMessageHandler;
import ru.sadykov.friendservice.friend.repository.FriendRepository;
import ru.sadykov.friendservice.friend.responsemessage.localization.LocalizationResponseMessage;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class FriendServiceImplTest {

    @InjectMocks
    private FriendServiceImpl friendService;
    @Mock
    private FriendRepository friendRepository;
    @Mock
    private FriendFinder friendFinder;
    @Spy
    private FriendFactory friendFactory;
    @Mock
    private ResponseMessageHandler responseMessageHandler;
    @Spy
    private LocalizationExceptionMessage localizationExceptionMessage;
    @Spy
    private LocalizationResponseMessage localizationResponseMessage;


    @Test
    void createNewFollower() {
        long userId = 1L;
        long subscriberId = 2L;
        String expected = localizationResponseMessage.getYouAreSub();

        when(friendFinder.findFriend(userId, subscriberId)).thenReturn(Optional.empty());
        when(friendRepository.save(any())).thenReturn(getForCreation());

        String response = friendService.addFriend(userId, subscriberId);

        assertEquals(expected, response);
        verify(friendFinder, times(1)).findFriend(userId, subscriberId);
        verify(friendRepository, times(1)).save(any());
    }

    @Test
    void addFriend() {
        long userId = 1L;
        long subscriberId = 2L;

        String excepted = localizationResponseMessage.getAreYouFriend();

        when(friendFinder.findFriend(subscriberId, userId)).thenReturn(Optional.of(getForAddFriend()));
        when(friendRepository.save(any())).thenReturn(getForAddFriendStatusFriend());

        String response = friendService.addFriend(subscriberId, userId);

        assertEquals(excepted, response);
    }

    @Test
    void youAreFriend() {
        long userId = 1L;
        long subscriberId = 2L;
        Friend friend = getForYouAreFriend();

        String excepted = localizationResponseMessage.getAlreadyFriend();

        when(friendFinder.findFriend(userId, subscriberId)).thenReturn(Optional.of(friend));
        when(responseMessageHandler.handle(friend, subscriberId)).thenReturn(Optional.of(excepted));

        String response = friendService.addFriend(userId, subscriberId);

        assertEquals(excepted, response);
    }

    @Test
    void reSub() {
        long userId = 1L;
        long subscriberId = 2L;
        Friend friend = getForReSub();

        String excepted = localizationResponseMessage.getRequestAlreadySent();

        when(friendFinder.findFriend(userId, subscriberId)).thenReturn(Optional.of(friend));
        when(responseMessageHandler.handle(friend, subscriberId)).thenReturn(Optional.of(excepted));

        String response = friendService.addFriend(userId, subscriberId);


        assertEquals(excepted, response);
    }

    @Test
    void ifUserIdEqualsFollowerId() {
        long userId = 1L;
        long subscriberId = 1L;

        assertThrows(InvalidRequestParameterException.class, () ->  friendService.addFriend(userId, subscriberId));
    }

    private Friend getForCreation() {
        Friend friend = new Friend();
        friend.setUserId(1L);
        friend.setSubscriberId(2L);
        friend.setStatus(Status.SUBSCRIBER);

        return friend;
    }

    private Friend getForAddFriend() {
        Friend friend = new Friend();
        friend.setUserId(1L);
        friend.setSubscriberId(2L);
        friend.setStatus(Status.SUBSCRIBER);

        return friend;
    }

    private Friend getForAddFriendStatusFriend() {
        Friend friend = new Friend();
        friend.setUserId(1L);
        friend.setSubscriberId(2L);
        friend.setStatus(Status.FRIEND);

        return friend;
    }

    private Friend getForYouAreFriend() {
        Friend friend = new Friend();
        friend.setUserId(1L);
        friend.setSubscriberId(2L);
        friend.setStatus(Status.FRIEND);

        return friend;
    }

    private Friend getForReSub() {
        Friend friend = new Friend();
        friend.setUserId(1L);
        friend.setSubscriberId(2L);
        friend.setStatus(Status.SUBSCRIBER);

        return friend;
    }
}