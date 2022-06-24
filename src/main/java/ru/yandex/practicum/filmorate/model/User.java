package ru.yandex.practicum.filmorate.model;

import lombok.Data;
import lombok.EqualsAndHashCode;
import ru.yandex.practicum.filmorate.validation.*;

import javax.validation.constraints.*;
import java.time.LocalDate;

@Data
@EqualsAndHashCode(callSuper = true)
@IdValidation(value = "user", groups = {Update.class})
public class User extends Id {
    @NotNull(message = "Email is required", groups = {Create.class})
    @Email(message = "Invalid email", groups = {Create.class, Update.class})
    @UsedEmailValidation(groups = {Create.class, Update.class})
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
