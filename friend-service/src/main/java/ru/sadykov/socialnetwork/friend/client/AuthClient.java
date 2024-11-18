package ru.sadykov.socialnetwork.friend.client;

import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.service.annotation.GetExchange;

public interface AuthClient {

    @GetExchange("/api/v1/auth/{userId}")
    boolean userIsExists(@PathVariable long userId);
}
