package ru.sadykov.notification;

import org.springframework.boot.SpringApplication;
import ru.sadykov.socialnetwork.NotificationServiceApplication;

public class TestNotificationServiceApplication {

	public static void main(String[] args) {
		SpringApplication.from(NotificationServiceApplication::main).with(TestcontainersConfiguration.class).run(args);
	}

}
