package ru.yandex.practicum.filmorate.model;

import lombok.Data;
import ru.yandex.practicum.filmorate.validation.Create;
import ru.yandex.practicum.filmorate.validation.Update;
import ru.yandex.practicum.filmorate.validation.CreationDateValidation;
import ru.yandex.practicum.filmorate.validation.CreatedFilmValidation;

import javax.validation.constraints.*;
import java.time.*;

@Data
public class Film {
    @CreatedFilmValidation(groups = {Update.class})
    int id;

    @NotBlank(message = "Name should be not blank", groups = {Create.class})
    String name;

    @NotNull(message = "Description is required", groups = {Create.class})
    @Size(
            max = 200,
            message = "Description should be less 200 then characters",
            groups = {Create.class, Update.class}
    )
    String description;

    @NotNull(message = "Release is required", groups = {Create.class})
    // for tests, but there should be: @JsonProperty("release_date")
    @CreationDateValidation(groups = {Create.class, Update.class})
    LocalDate releaseDate;

    @NotNull(message = "Duration is required", groups = {Create.class})
    @Positive(message = "Duration should be positive", groups = {Create.class, Update.class})
    Long duration;
}
