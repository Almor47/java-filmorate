package ru.yandex.practicum.filmorate.exceptions;

public class ErrorResponse {
    String response;
    String description;

    public ErrorResponse(String response, String description) {
        this.response = response;
        this.description = description;
    }

    public String getResponse() {
        return response;
    }

    public String getDescription() {
        return description;
    }
}
