package ru.yandex.practicum.filmorate.validation;

import ru.yandex.practicum.filmorate.data.inMemoryData;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;

public class UsedEmailValidator implements ConstraintValidator<UsedEmailValidation, String> {
    public boolean isValid(String email, ConstraintValidatorContext cxt) {
        return !inMemoryData.getInstance().containsEmail(email);
    }
}