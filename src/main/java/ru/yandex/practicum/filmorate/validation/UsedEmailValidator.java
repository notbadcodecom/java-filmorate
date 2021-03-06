package ru.yandex.practicum.filmorate.validation;

import org.springframework.beans.factory.annotation.Autowired;
import ru.yandex.practicum.filmorate.storage.UserStorage;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;

public class UsedEmailValidator implements ConstraintValidator<UsedEmailValidation, String> {

    private final UserStorage storage;

    @Autowired
    public UsedEmailValidator(UserStorage storage) {
        this.storage = storage;
    }

    public boolean isValid(String email, ConstraintValidatorContext cxt) {
        return storage.getAll().stream().noneMatch(u -> u.getEmail().equals(email));
    }
}
