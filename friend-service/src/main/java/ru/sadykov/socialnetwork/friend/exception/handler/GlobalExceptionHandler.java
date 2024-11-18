package ru.sadykov.socialnetwork.friend.exception.handler;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import ru.sadykov.socialnetwork.friend.exception.InvalidRequestParameterException;
import ru.sadykov.socialnetwork.friend.exception.UserNotFoundException;
import ru.sadykov.socialnetwork.friend.exception.dto.ExceptionMessageDto;

@RestControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(InvalidRequestParameterException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public ExceptionMessageDto handleInvalidRequestParameterException(InvalidRequestParameterException exc) {
        return new ExceptionMessageDto(exc.getMessage());
    }

    @ExceptionHandler(UserNotFoundException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public ExceptionMessageDto handleUserNotFoundException(UserNotFoundException exc) {
        return new ExceptionMessageDto(exc.getMessage());
    }
}