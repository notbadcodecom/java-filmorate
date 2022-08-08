package ru.yandex.practicum.filmorate.model;

import lombok.*;
import lombok.experimental.SuperBuilder;
import ru.yandex.practicum.filmorate.validation.*;


import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Positive;
import javax.validation.constraints.Size;
import java.time.LocalDate;
import java.util.List;

@NoArgsConstructor
@Setter
@Getter
@EqualsAndHashCode(callSuper = true)
@ToString
@SuperBuilder
public class Film extends Id {
    @NotBlank(message = "Name should be not blank", groups = {Create.class})
    String name;

    @NotNull(message = "Description is required", groups = {Create.class})
    @Size(
            max = 200,
            message = "Description should be less {max} characters",
            groups = {Create.class, Update.class}
    )
    String description;

    @NotNull(message = "Release is required", groups = {Create.class})
    // for tests, but there should be: @JsonProperty("release_date")
    @LocalDateMinValidation(value = "1895-12-28", groups = {Create.class, Update.class})
    LocalDate releaseDate;

    @NotNull(message = "Duration is required", groups = {Create.class})
    @Positive(message = "Duration should be positive", groups = {Create.class, Update.class})
    long duration;

    @NotNull(message = "MPA rating is required", groups = {Create.class})
    Mpa mpa;

    List<Genre> genres;
}
