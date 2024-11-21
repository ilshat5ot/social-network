package ru.sadykov.socialnetwork.notification.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Service;
import ru.sadykov.socialnetwork.friend.event.FriendAddEvent;

@Service
@Slf4j
@RequiredArgsConstructor
public class NotificationService {

    @KafkaListener(topics = "friendship-notification")
    public void listen(FriendAddEvent event) {
        log.info("Get message from notification topic {}", event);
        System.out.println(event.toString());

    }
}
