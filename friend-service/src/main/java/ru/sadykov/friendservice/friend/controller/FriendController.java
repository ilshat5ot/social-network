package ru.sadykov.friendservice.friend.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;
import ru.sadykov.friendservice.friend.service.FriendService;
import ru.sadykov.friendservice.friend.service.dto.FriendResponseDto;

@RestController
@RequestMapping("/api/v1")
@RequiredArgsConstructor
public class FriendController {

    private final FriendService friendService;

    @PostMapping("/{userId}/{followerId}")
    @ResponseStatus(HttpStatus.OK)
    public FriendResponseDto addFriend(@PathVariable long userId, @PathVariable long followerId) {
        return friendService.addFriend(userId, followerId);
    }
}
