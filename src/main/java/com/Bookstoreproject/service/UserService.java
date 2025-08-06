package com.Bookstoreproject.service;

import com.Bookstoreproject.beans.LoginRequest;
import com.Bookstoreproject.beans.RegisterRequest;
import com.Bookstoreproject.beans.UserResponse;

public interface UserService {
    UserResponse registerUser(RegisterRequest request);
    UserResponse authenticateUser(LoginRequest request);
    UserResponse getUserById(Long id);
}
