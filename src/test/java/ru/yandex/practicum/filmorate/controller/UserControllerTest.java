package ru.yandex.practicum.filmorate.controller;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import ru.yandex.practicum.filmorate.model.User;
import ru.yandex.practicum.filmorate.storage.InMemoryUserStorage;

import java.time.LocalDate;
import java.util.Arrays;
import java.util.HashSet;

import static org.hamcrest.collection.IsCollectionWithSize.hasSize;
import static org.junit.jupiter.api.Assertions.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
class UserControllerTest {

    private final MockMvc mockMvc;
    private final InMemoryUserStorage userStorage;

    @Autowired
    public UserControllerTest(
            MockMvc mockMvc, InMemoryUserStorage userStorage
    ) {
        this.mockMvc = mockMvc;
        this.userStorage = userStorage;
    }

    @Test
    @DisplayName("POST create user at /users")
    public void shouldAddAndReturnUser() throws Exception {
        mockMvc.perform(post("/users")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\"email\": \"user@domen.com\", " +
                                "\"login\": \"user-login\", " +
                                "\"name\": \"User Name\"," +
                                "\"birthday\": \"1988-04-01\"}"))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.id").hasJsonPath())
                .andExpect(jsonPath("$.email").value("user@domen.com"))
                .andExpect(jsonPath("$.login").value("user-login"))
                .andExpect(jsonPath("$.name").value("User Name"))
                .andExpect(jsonPath("$.birthday").value("1988-04-01"));
    }

    @Test
    @DisplayName("POST create user without name at /users")
    public void shouldAddAndReturnUser_whereLoginEqualsName() throws Exception {
        mockMvc.perform(post("/users")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\"email\": \"user@other-domen.com\", " +
                                "\"login\": \"user-login\", " +
                                "\"birthday\": \"1988-04-01\"}"))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.login").value("user-login"))
                .andExpect(jsonPath("$.name").value("user-login"));
    }

    @Test
    @DisplayName("POST create user without email at /users")
    public void shouldReturnErrorMessageIfNoEmail() throws Exception {
        mockMvc.perform(post("/users")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\"login\": \"user-login\", " +
                                "\"name\": \"User Name\"," +
                                "\"birthday\": \"1988-04-01\"}"))
                .andExpect(status().is4xxClientError())
                .andExpect(jsonPath("$.email").value("Email is required"));
    }

    @Test
    @DisplayName("POST create user with invalid email at /users")
    public void shouldReturnErrorMessageIfInvalidEmail() throws Exception {
        mockMvc.perform(post("/users")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\"email\": \"user_com\", " +
                                "\"login\": \"user-login\", " +
                                "\"name\": \"User Name\"," +
                                "\"birthday\": \"1988-04-01\"}"))
                .andExpect(status().is4xxClientError())
                .andExpect(jsonPath("$.email").value("Invalid email"));
    }

    @Test
    @DisplayName("POST create user without login at /users")
    public void shouldReturnErrorMessageIfNoLogin() throws Exception {
        mockMvc.perform(post("/users")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\"name\": \"User Name\"," +
                                "\"birthday\": \"1988-04-01\"}"))
                .andExpect(status().is4xxClientError())
                .andExpect(jsonPath("$.login").value("Login is required"));
    }

    @Test
    @DisplayName("POST create user with invalid login at /users")
    public void shouldReturnErrorMessageIfInvalidLogin() throws Exception {
        mockMvc.perform(post("/users")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\"email\": \"user5@domen.com\", " +
                                "\"login\": \"user.login_login_login-login-login-login-login-login\", " +
                                "\"name\": \"User Name\"," +
                                "\"birthday\": \"1988-04-01\"}"))
                .andExpect(status().is4xxClientError())
                .andExpect(jsonPath("$.login").value("Login consists of letters, numbers, dash and 3-20 characters"));
    }

    @Test
    @DisplayName("POST create user with birthday in future at /users")
    public void shouldReturnErrorMessageIfInvalidBirthday() throws Exception {
        mockMvc.perform(post("/users")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\"email\": \"user@domen.com\", " +
                                "\"login\": \"user-login\", " +
                                "\"name\": \"User Name\"," +
                                "\"birthday\": \"2088-04-01\"}"))
                .andExpect(status().is4xxClientError())
                .andExpect(jsonPath("$.birthday").value("Birthday can't be in the future"));
    }

    @Test
    @DisplayName("PUT update user at /users")
    public void shouldUpdateAndReturnUser() throws Exception {

        User user = userStorage.add(
                User.builder()
                        .email("mail31@test.com")
                        .birthday(LocalDate.of(1999,9,9))
                        .login("user")
                        .build()
        );

        mockMvc.perform(put("/users")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\"id\": " + user.getId() + ", " +
                                "\"email\": \"updated@email.com\", " +
                                "\"login\": \"Updated-login\", " +
                                "\"name\": \"Updated Name\"," +
                                "\"birthday\": \"1988-06-15\"}"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(user.getId()))
                .andExpect(jsonPath("$.email").value("updated@email.com"))
                .andExpect(jsonPath("$.login").value("Updated-login"))
                .andExpect(jsonPath("$.name").value("Updated Name"))
                .andExpect(jsonPath("$.birthday").value("1988-06-15"));
    }

    @Test
    @DisplayName("PUT update user without id at /users")
    public void shouldReturnErrorMessageIfNoId() throws Exception {
        mockMvc.perform(put("/users")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\"email\": \"user@domen.com\", " +
                                "\"login\": \"user-login\", " +
                                "\"name\": \"User Name\"," +
                                "\"birthday\": \"2088-04-01\"}"))
                .andExpect(status().is4xxClientError())
                .andExpect(jsonPath("$.id").value("Invalid ID or no user with this ID"));
    }

    @Test
    @DisplayName("PUT update user without id at /users")
    public void shouldReturnErrorMessageIfInvalidId() throws Exception {
        mockMvc.perform(put("/users")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\"id\": 999, " +
                                "\"email\": \"user@domen.com\", " +
                                "\"login\": \"user-login\", " +
                                "\"name\": \"User Name\"," +
                                "\"birthday\": \"2088-04-01\"}"))
                .andExpect(status().is4xxClientError())
                .andExpect(jsonPath("$.id").value("Invalid ID or no user with this ID"));
    }

    @Test
    @DisplayName("POST user with already used email at /users")
    public void shouldReturnErrorMessageIfEmailInUse() throws Exception {
        mockMvc.perform(post("/users")
                .contentType(MediaType.APPLICATION_JSON)
                .content("{\"email\": \"user@domen.com\", " +
                        "\"login\": \"user-login\", " +
                        "\"name\": \"User Name\"," +
                        "\"birthday\": \"2088-04-01\"}"));

        mockMvc.perform(post("/users")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\"email\": \"user@domen.com\", " +
                                "\"login\": \"user-login\", " +
                                "\"name\": \"User Name\"," +
                                " \"birthday\": \"2088-04-01\"}"))
                .andExpect(status().is4xxClientError())
                .andExpect(jsonPath("$.email").value("Email already in use"));
    }

    @Test
    @DisplayName("PUT and DELETE user like at /{id}/friends/{friendId}")
    public void shouldAddDeleteLikesOfBothUsers() throws Exception {

        User user = userStorage.add(User.builder().email("test1@ya.ru").build());
        User friend = userStorage.add(User.builder().email("test2@ya.ru").build());

        mockMvc.perform(put("/users/" + user.getId() + "/friends/" + friend.getId()))
                .andExpect(status().isOk());

        assertTrue(
                userStorage.loadLikes(user.getId()).orElseGet(HashSet::new).contains(friend.getId()),
                "Friend was not added to user"
        );

        assertTrue(
                userStorage.loadLikes(friend.getId()).orElseGet(HashSet::new).contains(user.getId()),
                "User was not added to friend"
        );

        mockMvc.perform(delete("/users/" + user.getId() + "/friends/" + friend.getId()))
                .andExpect(status().isNoContent());

        assertFalse(
                userStorage.loadLikes(user.getId()).orElseGet(HashSet::new).contains(friend.getId()),
                "Friend was not deleted from user"
        );

        assertFalse(
                userStorage.loadLikes(friend.getId()).orElseGet(HashSet::new).contains(user.getId()),
                "User was not deleted from friend"
        );
    }

    @Test
    @DisplayName("GET all friends of user at /users/{id}/friends")
    public void shouldReturnAllUserFriends() throws Exception {

        User user = userStorage.add(User.builder().build());

        userStorage.saveLikes(
                user.getId(), new HashSet<>(Arrays.asList(
                        userStorage.add(User.builder().build()).getId(),
                        userStorage.add(User.builder().build()).getId(),
                        userStorage.add(User.builder().build()).getId()
                ))
        );

        mockMvc.perform(get("/users/" + user.getId() + "/friends"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.*", hasSize(3)));
    }

    @Test
    @DisplayName("GET common friends of user at /users/{id}/friends/common/{otherId}")
    public void shouldReturnCommonFriends() throws Exception {

        User user = userStorage.add(User.builder().email("mail1@test.com").build());
        User friend = userStorage.add(User.builder().email("mail2@test.com").build());
        User commonFriend1 = userStorage.add(User.builder().email("mail3@test.com").build());
        User commonFriend2 = userStorage.add(User.builder().email("mail4@test.com").build());

        userStorage.saveLikes(
                user.getId(), new HashSet<>(Arrays.asList(
                        userStorage.add(User.builder().email("mail5@test.com").build()).getId(),
                        userStorage.add(User.builder().email("mail6@test.com").build()).getId(),
                        userStorage.add(User.builder().email("mail7@test.com").build()).getId(),
                        commonFriend1.getId(),
                        commonFriend2.getId()
                ))
        );

        userStorage.saveLikes(
                friend.getId(), new HashSet<>(Arrays.asList(
                        userStorage.add(User.builder().email("mail8@test.com").build()).getId(),
                        userStorage.add(User.builder().email("mail9@test.com").build()).getId(),
                        userStorage.add(User.builder().email("mail10@test.com").build()).getId(),
                        commonFriend1.getId(),
                        commonFriend2.getId()
                ))
        );

        mockMvc.perform(get("/users/" + user.getId() + "/friends/common/" + friend.getId()))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.*", hasSize(2)));
    }

}
