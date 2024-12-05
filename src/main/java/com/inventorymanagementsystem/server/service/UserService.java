package com.inventorymanagementsystem.server.service;

import java.util.Optional;

import com.inventorymanagementsystem.server.dto.request.AuthenticationRequest;
import com.inventorymanagementsystem.server.entities.User;

public interface UserService {
    User saveUser(User user);
    Optional<User> getUserById(String id);
    Optional<User> updateUser(User user);
    void deleteUser(String id);
    boolean isUserExistByEmail(String email);

    boolean isUserExistByEmailAndPassword(String email, String password);

    Optional<User> getUserByEmail(String email);
    User register(User user);
    void verify(String email, String otp);
    User login(String email, String password);
    User loginSecurity(AuthenticationRequest authenticationRequest);
}
