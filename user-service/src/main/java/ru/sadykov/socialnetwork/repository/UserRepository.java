package ru.sadykov.socialnetwork.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import ru.sadykov.socialnetwork.entity.Users;

public interface UserRepository extends JpaRepository<Users, Long> {

    boolean existsById(Long id);
}
