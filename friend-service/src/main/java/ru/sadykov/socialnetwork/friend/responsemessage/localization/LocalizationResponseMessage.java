package ru.sadykov.socialnetwork.friend.responsemessage.localization;

import lombok.Getter;
import org.springframework.stereotype.Component;

import java.util.Locale;

@Getter
@Component
public class LocalizationResponseMessage {

    private final ResponseMessageSource ms = new ResponseMessageSource();
    private final Locale locale;

    private final String youAreSub;
    private final String areYouFriend;
    private final String alreadyFriend;
    private final String requestAlreadySent;
    private final String defaultMessage;

    public LocalizationResponseMessage() {
        this.locale = Locale.getDefault();

        this.youAreSub = ms.getMessage("you.are.sub");
        this.areYouFriend = ms.getMessage("you.are.friend");
        this.alreadyFriend = ms.getMessage("already.friend");
        this.requestAlreadySent = ms.getMessage("request.already.sent");
        this.defaultMessage = ms.getMessage("default.message");
    }
}