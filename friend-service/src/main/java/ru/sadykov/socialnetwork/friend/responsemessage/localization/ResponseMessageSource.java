package ru.sadykov.socialnetwork.friend.responsemessage.localization;

import org.springframework.context.i18n.LocaleContextHolder;
import org.springframework.context.support.ResourceBundleMessageSource;

public class ResponseMessageSource extends ResourceBundleMessageSource {
    private static final String BASE_NAME = "ResponseMessages";

    public ResponseMessageSource() {
        this(BASE_NAME);
    }

    private ResponseMessageSource(String... baseNames) {
        super();
        setUseCodeAsDefaultMessage(true);
        setDefaultEncoding("UTF-8");
        addBasenames(baseNames);
    }

    public String getMessage(final String code, final Object... args) {
        return super.getMessage(code, args, LocaleContextHolder.getLocale());
    }
}
