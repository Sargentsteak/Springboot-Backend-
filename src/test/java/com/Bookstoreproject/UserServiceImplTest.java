package com.Bookstoreproject;


import com.Bookstoreproject.beans.LoginRequest;
import com.Bookstoreproject.beans.RegisterRequest;
import com.Bookstoreproject.beans.UserResponse;
import com.Bookstoreproject.entity.User;
import com.Bookstoreproject.repository.jpa.UserDao;
import com.Bookstoreproject.serviceImpl.UserServiceImpl;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.*;

import java.time.LocalDateTime;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class UserServiceImplTest {

    @Mock
    private UserDao userRepository;

    @InjectMocks
    private UserServiceImpl userService;

    @BeforeEach
    void setup() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void testRegisterUser_Success() {
        RegisterRequest request = new RegisterRequest();
        request.setEmail("test@example.com");
        request.setFullname("Test User");
        request.setPassword("123456");

        when(userRepository.existsByEmail("test@example.com")).thenReturn(false);
        when(userRepository.save(any())).thenAnswer(invocation -> {
            User user = invocation.getArgument(0);
            user.setId(1L);
            return user;
        });

        UserResponse response = userService.registerUser(request);

        assertNotNull(response);
        assertEquals("test@example.com", response.getEmail());
        assertEquals("Test User", response.getFullName());

        verify(userRepository).save(any());
    }

    @Test
    void testRegisterUser_EmailAlreadyExists() {
        RegisterRequest request = new RegisterRequest();
        request.setEmail("existing@example.com");
        request.setFullname("Someone");
        request.setPassword("password");

        when(userRepository.existsByEmail("existing@example.com")).thenReturn(true);

        assertThrows(IllegalArgumentException.class, () -> userService.registerUser(request));
        verify(userRepository, never()).save(any());
    }

    @Test
    void testAuthenticateUser_Success() {
        LoginRequest request = new LoginRequest();
        request.setEmail("auth@example.com");
        request.setPassword("secret");

        User user = new User();
        user.setId(42L);
        user.setEmail("auth@example.com");
        user.setFullName("Auth User");
        user.setPasswordHash("hashed_secret");
        user.setCreatedAt(LocalDateTime.now());

        when(userRepository.findByEmail("auth@example.com")).thenReturn(Optional.of(user));

        UserResponse response = userService.authenticateUser(request);

        assertNotNull(response);
        assertEquals("auth@example.com", response.getEmail());
        assertEquals("Auth User", response.getFullName());

        verify(userRepository).findByEmail("auth@example.com");
    }

    @Test
    void testAuthenticateUser_InvalidPassword() {
        LoginRequest request = new LoginRequest();
        request.setEmail("auth@example.com");
        request.setPassword("wrongpass");

        User user = new User();
        user.setEmail("auth@example.com");
        user.setPasswordHash("hashed_correctpass");

        when(userRepository.findByEmail("auth@example.com")).thenReturn(Optional.of(user));

        assertThrows(IllegalArgumentException.class, () -> userService.authenticateUser(request));
    }

    @Test
    void testAuthenticateUser_UserNotFound() {
        LoginRequest request = new LoginRequest();
        request.setEmail("notfound@example.com");
        request.setPassword("any");

        when(userRepository.findByEmail("notfound@example.com")).thenReturn(Optional.empty());

        assertThrows(IllegalArgumentException.class, () -> userService.authenticateUser(request));
    }

    @Test
    void testGetUserById_Success() {
        User user = new User();
        user.setId(123L);
        user.setEmail("findme@example.com");
        user.setFullName("Finder");

        when(userRepository.findById(123L)).thenReturn(Optional.of(user));

        UserResponse response = userService.getUserById(123L);

        assertEquals(123L, response.getId());
        assertEquals("findme@example.com", response.getEmail());
        assertEquals("Finder", response.getFullName());

        verify(userRepository).findById(123L);
    }

    @Test
    void testGetUserById_NotFound() {
        when(userRepository.findById(999L)).thenReturn(Optional.empty());

        assertThrows(IllegalArgumentException.class, () -> userService.getUserById(999L));
    }
}

