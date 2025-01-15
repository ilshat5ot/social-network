package ru.sadykov.socialnetwork.friend.client;

import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.service.annotation.GetExchange;

public interface AuthClient {

    @GetExchange("http://localhost:8002/userExists/{userId}")
    boolean userIsExists(@PathVariable long userId);
}
