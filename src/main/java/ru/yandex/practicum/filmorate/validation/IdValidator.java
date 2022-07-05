package ru.yandex.practicum.filmorate.validation;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.model.Id;
import ru.yandex.practicum.filmorate.model.User;
import ru.yandex.practicum.filmorate.storage.Storage;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;

@Slf4j
public class IdValidator implements ConstraintValidator<IdValidation, Id> {

    String className;

    Storage<Film> filmStorage;
    Storage<User> userStorage;

    @Autowired
    public IdValidator(Storage<Film> filmStorage, Storage<User> userStorage) {
        this.filmStorage = filmStorage;
        this.userStorage = userStorage;
    }


    @Override
    public void initialize(IdValidation className) {
        this.className = className.value();
    }

    @Override
    public boolean isValid(Id o, ConstraintValidatorContext cxt) {
        cxt.buildConstraintViolationWithTemplate(cxt.getDefaultConstraintMessageTemplate())
                .addPropertyNode("id")
                .addConstraintViolation();
        log.debug(o.toString());
        log.debug(className);
        switch (className) {
            case "film":
                return filmStorage.get(o.getId()).isPresent();
            case "user":
                return userStorage.get(o.getId()).isPresent();
            default:
                return true;
        }
    }
}
