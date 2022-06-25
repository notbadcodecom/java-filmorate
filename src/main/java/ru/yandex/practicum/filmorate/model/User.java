package ru.yandex.practicum.filmorate.model;

import lombok.Data;
import ru.yandex.practicum.filmorate.validation.Create;
import ru.yandex.practicum.filmorate.validation.Update;
import ru.yandex.practicum.filmorate.validation.CreatedUserValidation;
import ru.yandex.practicum.filmorate.validation.UsedEmailValidation;

import javax.validation.constraints.*;
import java.time.LocalDate;

@Data
public class User {
    @NotNull(message = "ID is required", groups = {Update.class})
    @CreatedUserValidation(groups = {Update.class})
    int id;

    @NotNull(message = "Email is required", groups = {Create.class})
    @Email(message = "Invalid email", groups = {Create.class, Update.class})
    @UsedEmailValidation(groups = {Update.class})
    String email;

    @NotBlank(message = "Login is required", groups = {Create.class})
    @Pattern(
            regexp = "^(?=.{3,20}$)(?![-])[a-zA-Z0-9-]+(?<![-])$",
            message = "Login consists of letters, numbers, dash and 3-20 characters",
            groups = {Create.class, Update.class}
    )
    String login;

    String name;

    @Past(message = "Birthday can't be in the future", groups = {Create.class, Update.class})
    LocalDate birthday;
}
