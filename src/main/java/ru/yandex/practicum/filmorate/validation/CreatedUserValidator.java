package ru.yandex.practicum.filmorate.validation;

import ru.yandex.practicum.filmorate.data.InMemoryData;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;

public class CreatedUserValidator implements ConstraintValidator<CreatedUserValidation, Integer> {
    public boolean isValid(Integer id, ConstraintValidatorContext cxt) {
        return InMemoryData.getInstance().getUser(id) != null;
    }
}
