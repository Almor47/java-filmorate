package ru.yandex.practicum.filmorate.exceptions;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
public class NoUpdateException extends RuntimeException {

    public NoUpdateException(String message) {
        super(message);
    }

    public NoUpdateException() {

    }

}
