package ru.sadykov.socialnetwork.service;

import ru.sadykov.socialnetwork.entity.Users;
import ru.sadykov.socialnetwork.service.dto.UserDto;

public interface UserService {

    Users createUser(Users user);

    Users updateUser(Users user);

    Users getById(Long id);

    void deleteUser(Long id);

    boolean userExists(Long userId);
}
