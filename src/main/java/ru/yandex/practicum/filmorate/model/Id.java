package ru.yandex.practicum.filmorate.model;

import lombok.Data;
import ru.yandex.practicum.filmorate.validation.Create;

import javax.validation.constraints.Max;

@Data
public abstract class Id {
    @Max(value = 0, message = "Should be no id for new object", groups = {Create.class})
    int id;
}
