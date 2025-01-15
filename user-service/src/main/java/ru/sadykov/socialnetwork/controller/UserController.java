package ru.sadykov.socialnetwork.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.ValueOperations;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;
import ru.sadykov.socialnetwork.entity.Users;
import ru.sadykov.socialnetwork.service.UserServiceImpl;
import ru.sadykov.socialnetwork.service.dto.UserDto;

@RestController
@RequestMapping
@RequiredArgsConstructor
public class UserController {

    private final UserServiceImpl userService;
    private final RedisTemplate<String, Users> redisTemplate;

    @PostMapping
    public UserDto createUser(@RequestBody UserDto userDto) {
        Users user = new Users();
        user.setName(userDto.name());
        Users savedUser = userService.createUser(user);
        return new UserDto(savedUser.getId(), savedUser.getName());
    }

    @PutMapping
    public UserDto updateUser(@RequestBody UserDto userDto) {
        Users user = new Users(userDto.id(), userDto.name());
        Users updatedUser = userService.updateUser(user);
        return new UserDto(updatedUser.getId(), updatedUser.getName());
    }

    @DeleteMapping("{userId}")
    public void deleteUser(@PathVariable Long userId) {
        userService.deleteUser(userId);
    }

    @GetMapping("{userId}")
    @ResponseStatus(HttpStatus.OK)
    public Users getById(@PathVariable Long userId) {
        return userService.getById(userId);
    }

    @GetMapping("userExists/{userId}")
    @ResponseStatus(HttpStatus.OK)
    public boolean userExists(@PathVariable Long userId) {
        return userService.userExists(userId);
    }

    @GetMapping("cache/{userId}")
    @ResponseStatus(HttpStatus.OK)
    public Users getCache(@PathVariable Long userId) {
        Users o = redisTemplate.opsForValue().get("UserService::getById::" + userId);
        return o;
    }


}
