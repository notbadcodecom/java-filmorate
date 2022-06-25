package ru.yandex.practicum.filmorate.validation;

import ru.yandex.practicum.filmorate.data.inMemoryData;
import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;

public class CreatedFilmValidator implements ConstraintValidator<CreatedFilmValidation, Integer> {
    public boolean isValid(Integer id, ConstraintValidatorContext cxt) {
        return inMemoryData.getInstance().getFilm(id) != null;
    }
}