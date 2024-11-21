package ru.sadykov.socialnetwork.friend.exception.localization;

import lombok.Getter;
import org.springframework.stereotype.Component;

import java.util.Locale;

@Getter
@Component
public class LocalizationExceptionMessage {

    private final ExceptionMessageSource ms = new ExceptionMessageSource();
    private final Locale locale;

    private final String addYourselfExc;
    private final String userNotFound;

    public LocalizationExceptionMessage() {
        this.locale = Locale.getDefault();

        this.addYourselfExc = ms.getMessage("add.yourself");
        this.userNotFound = ms.getMessage("user.not.found");
    }
}
