package ru.sadykov.friendservice.friend.exception;

public class InvalidRequestParameterException extends RuntimeException {

    public InvalidRequestParameterException() {
    }

    public InvalidRequestParameterException(String message) {
        super(message);
    }
}
