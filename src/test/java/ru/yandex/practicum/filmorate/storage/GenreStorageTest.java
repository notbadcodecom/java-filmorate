package ru.yandex.practicum.filmorate.storage;

import lombok.RequiredArgsConstructor;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.context.SpringBootTest;
import ru.yandex.practicum.filmorate.model.Genre;

import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest
@AutoConfigureTestDatabase
@RequiredArgsConstructor(onConstructor_ = @Autowired)
class GenreStorageTest {

    private final GenreStorage genreStorage;

    @Test
    @DisplayName("Get genre by id")
    void shouldReturnGenre() {
        Optional<Genre> genreOptional = genreStorage.loadGenreById(1);
        assertThat(genreOptional)
                .isPresent()
                .hasValueSatisfying(user ->
                        assertThat(user).hasFieldOrPropertyWithValue("id", 1L)
                );
    }

    @Test
    @DisplayName("Get genre size 6")
    void shouldReturnArrayListGenreSize_6() {
        List<Genre> genre = genreStorage.loadAllGenres();
        assertThat(genre).hasSize(6);
    }
}
