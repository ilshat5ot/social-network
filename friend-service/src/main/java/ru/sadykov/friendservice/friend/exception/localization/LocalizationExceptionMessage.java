package ru.sadykov.friendservice.friend.exception.localization;

import lombok.Getter;
import org.springframework.stereotype.Component;

import java.util.Locale;

@Getter
@Component
public class LocalizationExceptionMessage {

    private final ExceptionMessageSource ms = new ExceptionMessageSource();
    private final Locale locale;

    private final String addYourselfExc;

    public LocalizationExceptionMessage() {
        this.locale = Locale.getDefault();

        this.addYourselfExc = ms.getMessage("add.yourself");
    }
}
