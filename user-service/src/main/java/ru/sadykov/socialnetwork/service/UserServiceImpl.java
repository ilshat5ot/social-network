package ru.sadykov.socialnetwork.service;

import lombok.RequiredArgsConstructor;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.CachePut;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.cache.annotation.Caching;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.sadykov.socialnetwork.entity.Users;
import ru.sadykov.socialnetwork.repository.UserRepository;

@Service
@RequiredArgsConstructor
public class UserServiceImpl implements UserService {

    private final UserRepository userRepository;

    @Transactional
    @Caching(cacheable = {
            @Cacheable(
                    value = "UserService::getById",
                    condition = "#user.id!=null",
                    key = "#user.id"
            )
    })
    @Override
    public Users createUser(Users user) {
        return userRepository.save(user);
    }

    @Transactional
    @Caching(put = {
            @CachePut(
                    value = "UserService::getById",
                    key = "#user.id"
            )
    })
    @Override
    public Users updateUser(Users user) {
        Long id = user.getId();

        Users existing = userRepository.findById(id)
                .orElseThrow(() -> new RuntimeException(String.format("User not found with id %s", id)));

        existing.setName(user.getName());

        return userRepository.save(existing);
    }

    @Transactional(readOnly = true)
    @Cacheable(
            value = "UserService::getById",
            key = "#id"
    )
    @Override
    public Users getById(Long id) {
        Users user = userRepository.findById(id)
                .orElseThrow(() -> new RuntimeException(String.format("User not found with id %s", id)));

        return user;
    }

    @CacheEvict(
            value = "UserService::getById",
            key = "#id"
    )
    @Override
    public void deleteUser(Long id) {

        userRepository.findById(id)
                .orElseThrow(() -> new RuntimeException(String.format("User not found with id %s", id)));

        userRepository.deleteById(id);
    }

    @Override
    public boolean userExists(Long userId) {
        return userRepository.existsById(userId);
    }
}
