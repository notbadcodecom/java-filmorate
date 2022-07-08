package ru.yandex.practicum.filmorate.controller;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.model.User;
import ru.yandex.practicum.filmorate.storage.InMemoryFilmStorage;
import ru.yandex.practicum.filmorate.storage.InMemoryUserStorage;

import java.util.Arrays;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import static org.hamcrest.collection.IsCollectionWithSize.hasSize;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;


@SpringBootTest
@AutoConfigureMockMvc
class FilmControllerTest {

    private final MockMvc mockMvc;
    private final InMemoryFilmStorage filmStorage;
    private final InMemoryUserStorage userStorage;

    @Autowired
    public FilmControllerTest(
            MockMvc mockMvc, InMemoryFilmStorage filmStorage, InMemoryUserStorage userStorage
    ) {
        this.mockMvc = mockMvc;
        this.filmStorage = filmStorage;
        this.userStorage = userStorage;
    }

    @Test
    @DisplayName("POST create film at /films")
    public void shouldAddAndReturnFilm() throws Exception {
        mockMvc.perform(post("/films")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\"name\": \"Monty Python and the Holy Grail\", " +
                                "\"description\": \"King Arthur search for the Holy Grail\", " +
                                "\"releaseDate\": \"1975-03-14\"," +
                                " \"duration\": 91}"))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.id").hasJsonPath())
                .andExpect(jsonPath("$.name").value("Monty Python and the Holy Grail"))
                .andExpect(jsonPath("$.description").value("King Arthur search for the Holy Grail"))
                .andExpect(jsonPath("$.releaseDate").value("1975-03-14"))
                .andExpect(jsonPath("$.duration").value(91));
    }

    @Test
    @DisplayName("POST create film without \"name\" field at /films")
    public void shouldReturnErrorMessageIfNoName() throws Exception {
        mockMvc.perform(post("/films")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\"description\": \"King Arthur search for the Holy Grail\", " +
                                "\"releaseDate\": \"1975-03-14\"," +
                                "\"duration\": 91}"))
                .andExpect(status().is4xxClientError())
                .andExpect(jsonPath("$.name").value("Name should be not blank"));
    }

    @Test
    @DisplayName("POST create film with blank \"name\" field at /films")
    public void shouldReturnErrorMessageIfBlankName() throws Exception {
        mockMvc.perform(post("/films")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\"name\": \"   \", " +
                                "\"description\": \"King Arthur search for the Holy Grail\", " +
                                "\"releaseDate\": \"1975-03-14\"," +
                                "\"duration\": 91}"))
                .andExpect(status().is4xxClientError())
                .andExpect(jsonPath("$.name").value("Name should be not blank"));
    }

    @Test
    @DisplayName("POST create film without \"description\" field at /films")
    public void shouldReturnErrorMessageIfNoDescription() throws Exception {
        mockMvc.perform(post("/films")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\"name\": \"Monty Python and the Holy Grail\", " +
                                "\"releaseDate\": \"1975-03-14\"," +
                                "\"duration\": 91}"))
                .andExpect(status().is4xxClientError())
                .andExpect(jsonPath("$.description").value("Description is required"));
    }

    @Test
    @DisplayName("POST create film with \"description\"s length equals 201 at /films")
    public void shouldReturnErrorMessageIfDescriptionLengthIs201() throws Exception {
        mockMvc.perform(post("/films")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\"name\": \"Monty Python and the Holy Grail\", " +
                                "\"description\": \"Lorem ipsum dolor sit amet, " +
                                "consectetur adipiscing elit, sed do eiusmod tempor " +
                                "incididunt ut labore et dolore magna aliqua. Ut enim " +
                                "ad minim veniam, quis nostrud exercitation ullamco " +
                                "laboris nisi ut al\", " +
                                "\"releaseDate\": \"1975-03-14\"," +
                                "\"duration\": 91}"))
                .andExpect(status().is4xxClientError())
                .andExpect(jsonPath("$.description")
                        .value("Description should be less 200 characters"));
    }

    @Test
    @DisplayName("POST create film with \"description\"s length equals 200 at /films")
    public void shouldAddAndReturnFilmIfDescriptionLengthIs200() throws Exception {
        mockMvc.perform(post("/films")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\"name\": \"Monty Python and the Holy Grail\", " +
                                "\"description\": \"Lorem ipsum dolor sit amet, " +
                                "consectetur adipiscing elit, sed do eiusmod tempor " +
                                "incididunt ut labore et dolore magna aliqua. Ut enim " +
                                "ad minim veniam, quis nostrud exercitation ullamco " +
                                "laboris nisi ut a\", " +
                                "\"releaseDate\": \"1975-03-14\"," +
                                "\"duration\": 91}"))
                .andExpect(status().isCreated());
    }

    @Test
    @DisplayName("POST create film without \"releaseDate\" field at /films")
    public void shouldReturnErrorMessageIfNoReleaseDate() throws Exception {
        mockMvc.perform(post("/films")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\"name\": \"Monty Python and the Holy Grail\", " +
                                "\"description\": \"King Arthur search for the Holy Grail\", " +
                                "\"duration\": 91}"))
                .andExpect(status().is4xxClientError())
                .andExpect(jsonPath("$.releaseDate").value("Release is required"));
    }

    @Test
    @DisplayName("POST create film with \"releaseDate\" before 1895-12-28 at /films")
    public void shouldReturnErrorMessageIfReleaseDateBefore1895_12_28() throws Exception {
        mockMvc.perform(post("/films")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\"name\": \"Monty Python and the Holy Grail\", " +
                                "\"description\": \"King Arthur search for the Holy Grail\", " +
                                "\"releaseDate\": \"1895-12-27\"," +
                                "\"duration\": 91}"))
                .andExpect(status().is4xxClientError())
                .andExpect(jsonPath("$.releaseDate").value("Movie should be released after 1895-12-28"));
    }

    @Test
    @DisplayName("POST create film with \"releaseDate\" equals 1895-12-28 at /films")
    public void shouldReturnErrorMessageIfReleaseDateEquals1895_12_28() throws Exception {
        mockMvc.perform(post("/films")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\"name\": \"Monty Python and the Holy Grail\", " +
                                "\"description\": \"King Arthur search for the Holy Grail\", " +
                                "\"releaseDate\": \"1895-12-28\"," +
                                "\"duration\": 91}"))
                .andExpect(status().isCreated());
    }

    @Test
    @DisplayName("POST create film without \"duration\" field at /films")
    public void shouldReturnErrorMessageIfNoDuration() throws Exception {
        mockMvc.perform(post("/films")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\"name\": \"Monty Python and the Holy Grail\", " +
                                "\"description\": \"King Arthur search for the Holy Grail\", " +
                                "\"releaseDate\": \"1975-03-14\"}"))
                .andExpect(status().is4xxClientError())
                .andExpect(jsonPath("$.duration").value("Duration is required"));
    }

    @Test
    @DisplayName("POST create film with negative \"duration\" field at /films")
    public void shouldReturnErrorMessageIfNegativeDuration() throws Exception {
        mockMvc.perform(post("/films")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\"name\": \"Monty Python and the Holy Grail\", " +
                                "\"description\": \"King Arthur search for the Holy Grail\", " +
                                "\"releaseDate\": \"1975-03-14\", " +
                                "\"duration\": -1}"))
                .andExpect(status().is4xxClientError())
                .andExpect(jsonPath("$.duration").value("Duration should be positive"));
    }

    @Test
    @DisplayName("PUT update film at /films")
    public void shouldUpdateAndReturnFilm() throws Exception {
        mockMvc.perform(put("/films")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\"id\": 1, " +
                                "\"name\": \"Updated name\", " +
                                "\"description\": \"Updated description\", " +
                                "\"releaseDate\": \"2000-03-14\"," +
                                " \"duration\": 191}"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(1))
                .andExpect(jsonPath("$.name").value("Updated name"))
                .andExpect(jsonPath("$.description").value("Updated description"))
                .andExpect(jsonPath("$.releaseDate").value("2000-03-14"))
                .andExpect(jsonPath("$.duration").value(191));
    }

    @Test
    @DisplayName("PUT update film without id at /films")
    public void shouldReturnErrorMessageIfNoId() throws Exception {
        mockMvc.perform(put("/films")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\"name\": \"Updated name\", " +
                                "\"description\": \"Updated description\", " +
                                "\"releaseDate\": \"2000-03-14\"," +
                                " \"duration\": 191}"))
                .andExpect(status().is4xxClientError())
                .andExpect(jsonPath("$.id").value("Invalid ID or no film with this ID"));
    }

    @Test
    @DisplayName("PUT update film with invalid id at /films")
    public void shouldReturnErrorMessageIfInvalidId() throws Exception {
        mockMvc.perform(put("/films")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\"id\": 999, " +
                                "\"name\": \"Updated name\", " +
                                "\"description\": \"Updated description\", " +
                                "\"releaseDate\": \"2000-03-14\"," +
                                " \"duration\": 191}"))
                .andExpect(status().is4xxClientError())
                .andExpect(jsonPath("$.id").value("Invalid ID or no film with this ID"));
    }

    @Test
    @DisplayName("PUT score to film with invalid id at /films/{id}/like/{userId}")
    public void shouldReturn404ThenAddScoreIfNoSuchFilm() throws Exception {
        User user = userStorage.add(User.builder().build());
        mockMvc.perform(put("/films/" + 999 + "/like/" + user.getId()))
                .andExpect(status().is4xxClientError());
    }

    @Test
    @DisplayName("PUT score to film with invalid user id at /films/{id}/like/{userId}")
    public void shouldReturn404ThenAddScoreIfNoSuchUser() throws Exception {
        Film film = filmStorage.add(Film.builder().build());
        mockMvc.perform(put("/films/" + film.getId() + "/like/" + 999))
                .andExpect(status().is4xxClientError());
    }

    @Test
    @DisplayName("PUT and DELETE score from film at /films/{id}/like/{userId}")
    public void shouldAddAndDeleteFilmScore() throws Exception {
        User user = userStorage.add(User.builder().build());
        Film film = filmStorage.add(Film.builder().build());

        mockMvc.perform(put("/films/" + film.getId() + "/like/" + user.getId()))
                .andExpect(status().isOk());
        Set<Integer> likes = filmStorage.loadScores(film.getId()).orElse(Set.of(-1));
        int count = likes.size();
        assertTrue(likes.contains(user.getId()), "Movie score not added!");

        mockMvc.perform(delete("/films/" + film.getId() + "/like/" + user.getId()))
                .andExpect(status().isNoContent());
        likes = filmStorage.loadScores(film.getId()).orElse(Set.of(-1));
        assertEquals(--count, likes.size(), "Movie score not deleted");
    }

    @Test
    @DisplayName("GET popular films at /films/popular")
    public void shouldReturnPopularFilm() throws Exception {

        Film lastScoresFilm = filmStorage.add(Film.builder().build());
        filmStorage.saveScores(lastScoresFilm.getId(), new HashSet<>(List.of(1)));
        filmStorage.saveScores(filmStorage.add(Film.builder().build()).getId(), new HashSet<>(Arrays.asList(1,2)));
        filmStorage.saveScores(filmStorage.add(Film.builder().build()).getId(), new HashSet<>(Arrays.asList(1,2,3)));
        filmStorage.saveScores(filmStorage.add(Film.builder().build()).getId(), new HashSet<>(Arrays.asList(1,2,3,4)));
        filmStorage.saveScores(
                filmStorage.add(Film.builder().build()).getId(), new HashSet<>(Arrays.asList(1,2,3,4,5))
        );
        filmStorage.saveScores(
                filmStorage.add(Film.builder().build()).getId(), new HashSet<>(Arrays.asList(1,2,3,4,5,6))
        );
        filmStorage.saveScores(
                filmStorage.add(Film.builder().build()).getId(), new HashSet<>(Arrays.asList(1,2,3,4,5,6,7))
        );
        filmStorage.saveScores(
                filmStorage.add(Film.builder().build()).getId(), new HashSet<>(Arrays.asList(1,2,3,4,5,6,7,8))
        );
        Film maxScoresFilm = filmStorage.add(Film.builder().build());
        filmStorage.saveScores(maxScoresFilm.getId(), new HashSet<>(Arrays.asList(1,2,3,4,5,6,7,8,9)));
        filmStorage.saveScores(filmStorage.add(Film.builder().build()).getId(), new HashSet<>());

        mockMvc.perform(get("/films/popular"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.*", hasSize(10)))
                .andExpect(jsonPath("$[0].id").value(maxScoresFilm.getId()))
                .andExpect(jsonPath("$[8].id").value(lastScoresFilm.getId()));

        mockMvc.perform(get("/films/popular?&count=5"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.*", hasSize(5)));
    }
}