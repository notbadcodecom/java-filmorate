package ru.yandex.practicum.filmorate.controller;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(UserController.class)
class UserControllerTest {

    @Autowired
    private MockMvc mockMvc;

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
                .andExpect(status().is5xxServerError())
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
                .andExpect(status().is5xxServerError())
                .andExpect(jsonPath("$.email").value("Invalid email"));
    }

    @Test
    @DisplayName("POST create user without login at /users")
    public void shouldReturnErrorMessageIfNoLogin() throws Exception {
        mockMvc.perform(post("/users")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\"name\": \"User Name\"," +
                                "\"birthday\": \"1988-04-01\"}"))
                .andExpect(status().is5xxServerError())
                .andExpect(jsonPath("$.login").value("Login is required"));
    }

    @Test
    @DisplayName("POST create user with invalid login at /users")
    public void shouldReturnErrorMessageIfInvalidLogin() throws Exception {
        mockMvc.perform(post("/users")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\"email\": \"user@domen.com\", " +
                                "\"login\": \"user.login_login_login-login-login-login-login-login\", " +
                                "\"name\": \"User Name\"," +
                                "\"birthday\": \"1988-04-01\"}"))
                .andExpect(status().is5xxServerError())
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
                .andExpect(status().is5xxServerError())
                .andExpect(jsonPath("$.birthday").value("Birthday can't be in the future"));
    }

    @Test
    @DisplayName("PUT update user at /users")
    public void shouldUpdateAndReturnUser() throws Exception {
        mockMvc.perform(put("/users")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\"id\": 1, " +
                                "\"email\": \"updated@email.com\", " +
                                "\"login\": \"Updated-login\", " +
                                "\"name\": \"Updated Name\"," +
                                "\"birthday\": \"1988-06-15\"}"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(1))
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
                .andExpect(status().is5xxServerError())
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
                .andExpect(status().is5xxServerError())
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
                .andExpect(status().is5xxServerError())
                .andExpect(jsonPath("$.email").value("Email already in use"));
    }
}