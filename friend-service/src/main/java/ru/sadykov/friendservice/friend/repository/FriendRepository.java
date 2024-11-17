package ru.sadykov.friendservice.friend.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.querydsl.QuerydslPredicateExecutor;
import org.springframework.stereotype.Repository;
import ru.sadykov.friendservice.friend.entity.Friend;

@Repository
public interface FriendRepository extends JpaRepository<Friend, Integer>, QuerydslPredicateExecutor<Friend> {

}
