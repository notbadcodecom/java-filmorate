package ru.yandex.practicum.filmorate.model;


import lombok.*;
import lombok.experimental.SuperBuilder;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

@NoArgsConstructor
@Setter
@Getter
@EqualsAndHashCode
@ToString
@SuperBuilder
public class Review {

    private long reviewId;//не заимствовал от ID т.к. тесты требуют чтоб поле называлось reviewId
    @NotBlank
    private String content;
    @NotNull
    private Boolean isPositive;
    @NotNull
    private Long userId;
    @NotNull
    private Long filmId;
    private int useful;
}
