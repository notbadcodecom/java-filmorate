package ru.yandex.practicum.filmorate.validation;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import ru.yandex.practicum.filmorate.model.User;
import ru.yandex.practicum.filmorate.storage.Storage;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;

public class UsedEmailValidator implements ConstraintValidator<UsedEmailValidation, String> {

    @Qualifier("userStorage")
    private final Storage<User> storage;

    @Autowired
    public UsedEmailValidator(Storage<User> storage) {
        this.storage = storage;
    }

    public boolean isValid(String email, ConstraintValidatorContext cxt) {
        return storage.getAll().stream().noneMatch(u -> u.getEmail().equals(email));
    }
}
