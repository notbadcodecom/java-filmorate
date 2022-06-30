package ru.yandex.practicum.filmorate.validation;

import ru.yandex.practicum.filmorate.data.InMemoryData;
import ru.yandex.practicum.filmorate.model.Id;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;


public class IdValidator implements ConstraintValidator<IdValidation, Id> {

    String className;

    @Override
    public void initialize(IdValidation className) {
        this.className = className.value();
    }

    @Override
    public boolean isValid(Id o, ConstraintValidatorContext cxt) {
        cxt.buildConstraintViolationWithTemplate(cxt.getDefaultConstraintMessageTemplate())
                .addPropertyNode("id")
                .addConstraintViolation();
        switch (className) {
            case "film":
                return InMemoryData.getInstance().getFilm(o.getId()) != null;
            case "user":
                return InMemoryData.getInstance().getUser(o.getId()) != null;
            default:
                return true;
        }
    }
}
