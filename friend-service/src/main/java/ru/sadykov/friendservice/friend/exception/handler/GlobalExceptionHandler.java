package ru.sadykov.friendservice.friend.exception.handler;

import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import ru.sadykov.friendservice.friend.exception.dto.ExceptionMessageDto;
import ru.sadykov.friendservice.friend.exception.InvalidRequestParameterException;

@RestControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(InvalidRequestParameterException.class)
    public ExceptionMessageDto handleInvalidRequestParameterException(InvalidRequestParameterException exc) {
        return new ExceptionMessageDto(exc.getMessage());
    }
}
