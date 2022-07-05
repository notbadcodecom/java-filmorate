package ru.yandex.practicum.filmorate.exception;

public class NotFoundException extends RuntimeException {

    public NotFoundException(String objectName) {
        super(objectName + " not found!");
    }
}
