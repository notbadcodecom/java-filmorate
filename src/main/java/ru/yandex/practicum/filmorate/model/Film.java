package ru.yandex.practicum.filmorate.model;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

import javax.validation.constraints.*;
import java.time.*;

@Data
public class Film {

    int id;

    @NotBlank(message = "Name should be not blank")
    String name;

    @NotNull(message = "Description is required")
    @Size(max = 200, message = "Description should be less 200 then characters")
    String description;

    @NotNull(message = "Release is required")
    @JsonProperty("release_date")
    @Min(value = 504565200L, message = "Movie should be released after 1985-12-28")
    Long releaseDate;

    @NotNull(message = "Duration is required")
    @Positive(message = "Duration should be positive")
    Long duration;

    public LocalDate getReleaseDate() {
        return Instant.ofEpochSecond(releaseDate).atOffset(ZoneOffset.UTC).toLocalDate();
    }

    public void setReleaseDate(LocalDate date) {
        releaseDate = date.toEpochSecond(LocalTime.ofSecondOfDay(0), ZoneOffset.UTC);
    }
}
