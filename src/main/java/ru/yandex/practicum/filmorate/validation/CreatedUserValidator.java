package ru.yandex.practicum.filmorate.validation;

import ru.yandex.practicum.filmorate.data.inMemoryData;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;

public class CreatedUserValidator implements ConstraintValidator<CreatedUserValidation, Integer> {
    public boolean isValid(Integer id, ConstraintValidatorContext cxt) {
        return inMemoryData.getInstance().getUser(id) != null;
    }
}