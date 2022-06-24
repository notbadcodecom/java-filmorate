package ru.yandex.practicum.filmorate.model;

import lombok.Data;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Past;
import java.time.LocalDate;

@Data
public class User {
    int id;

    @NotNull(message = "Email is required")
    @Email(message = "Invalid email")
    String email;

    @NotBlank(message = "Login is required")
    String login;

    String name;

    @Past(message = "Birthday can't be in the future")
    LocalDate birthday;
}
