package com.Bookstoreproject;
import com.Bookstoreproject.beans.LoginRequest;
import com.Bookstoreproject.beans.RegisterRequest;
import com.Bookstoreproject.beans.UserResponse;
import com.Bookstoreproject.controller.UserController;
import com.Bookstoreproject.service.UserService;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.MediaType;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.test.web.servlet.MockMvc;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;

import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(UserController.class)
class UserControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Mock
    private UserService userService;

    @Autowired
    private ObjectMapper objectMapper;

    private UserResponse dummyUser;

    @BeforeEach
    void setup() {
        dummyUser = new UserResponse();
        dummyUser.setId(1L);
        dummyUser.setEmail("john.doe@example.com");
        dummyUser.setFullName("John Doe");
    }

    @Test
    void testRegisterUser_Success() throws Exception {
        RegisterRequest request = new RegisterRequest();
        request.setEmail("john.doe@example.com");
        request.setFullname("John Doe");
        request.setPassword("password");

        when(userService.registerUser(any(RegisterRequest.class))).thenReturn(dummyUser);

        mockMvc.perform(post("/api/users/register")
                        .contentType(String.valueOf(MediaType.APPLICATION_JSON))
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(dummyUser.getId()))
                .andExpect(jsonPath("$.email").value(dummyUser.getEmail()))
                .andExpect(jsonPath("$.fullName").value(dummyUser.getFullName()));
    }

    @Test
    void testLoginUser_Success() throws Exception {
        LoginRequest request = new LoginRequest();
        request.setEmail("john.doe@example.com");
        request.setPassword("password");

        when(userService.authenticateUser(any(LoginRequest.class))).thenReturn(dummyUser);

        mockMvc.perform(post("/api/users/login")
                        .contentType(String.valueOf(MediaType.APPLICATION_JSON))
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(dummyUser.getId()))
                .andExpect(jsonPath("$.email").value(dummyUser.getEmail()))
                .andExpect(jsonPath("$.fullName").value(dummyUser.getFullName()));
    }

    @Test
    void testGetUserById_Success() throws Exception {
        when(userService.getUserById(1L)).thenReturn(dummyUser);

        mockMvc.perform(get("/api/users/1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(dummyUser.getId()))
                .andExpect(jsonPath("$.email").value(dummyUser.getEmail()))
                .andExpect(jsonPath("$.fullName").value(dummyUser.getFullName()));
    }

    @Test
    void testRegisterUser_EmailAlreadyExists() throws Exception {
        RegisterRequest request = new RegisterRequest();
        request.setEmail("existing@example.com");
        request.setFullname("User");
        request.setPassword("password");

        Mockito.when(userService.registerUser(any(RegisterRequest.class)))
                .thenThrow(new IllegalArgumentException("Email already in use"));

        mockMvc.perform(post("/api/users/register")
                        .contentType(String.valueOf(MediaType.APPLICATION_JSON))
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.message").value("Email already in use"));
    }
}