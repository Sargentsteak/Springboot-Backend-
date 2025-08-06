package com.Bookstoreproject.serviceImpl;

import com.Bookstoreproject.beans.LoginRequest;
import com.Bookstoreproject.beans.RegisterRequest;
import com.Bookstoreproject.beans.UserResponse;
import com.Bookstoreproject.entity.User;
import com.Bookstoreproject.repository.jpa.UserDao;
import com.Bookstoreproject.service.UserService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;

@Service
public class UserServiceImpl implements UserService {

    private final UserDao userRepository;

    public UserServiceImpl(UserDao userRepository) {
        this.userRepository = userRepository;
    }

    @Transactional
    @Override
    public UserResponse registerUser(RegisterRequest request) {
        if (userRepository.existsByEmail(request.getEmail())) {
            throw new IllegalArgumentException("Email already in use");
        }

        String fakeHash = "hashed_" + request.getPassword();

        User user = new User();
        user.setEmail(request.getEmail());
        user.setFullName(request.getFullname());
        user.setPasswordHash(fakeHash);
        user.setCreatedAt(LocalDateTime.now());

        User saved = userRepository.save(user);

        UserResponse response = new UserResponse();
        response.setId(saved.getId());
        response.setEmail(saved.getEmail());
        response.setFullName(saved.getFullName());
        return response;
    }

    @Override
    public UserResponse authenticateUser(LoginRequest request) {
        User user = userRepository.findByEmail(request.getEmail())
                .orElseThrow(() -> new IllegalArgumentException("Invalid credentials"));

        String expectedHash = "hashed_" + request.getPassword();
        if (!user.getPasswordHash().equals(expectedHash)) {
            throw new IllegalArgumentException("Invalid credentials");
        }

        UserResponse response = new UserResponse();
        response.setId(user.getId());
        response.setEmail(user.getEmail());
        response.setFullName(user.getFullName());
        return response;
    }

    @Override
    public UserResponse getUserById(Long id) {
        User user = userRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("User not found"));

        UserResponse response = new UserResponse();
        response.setId(user.getId());
        response.setEmail(user.getEmail());
        response.setFullName(user.getFullName());
        return response;
    }
}
