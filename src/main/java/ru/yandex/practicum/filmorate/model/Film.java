package ru.yandex.practicum.filmorate.model;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.ToString;
import ru.yandex.practicum.filmorate.validation.*;

import javax.validation.constraints.*;
import java.time.*;

@Data
@EqualsAndHashCode(callSuper = true)
@ToString(callSuper = true)
@IdValidation(value = "film", groups = {Update.class})
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
    Long duration;
}
