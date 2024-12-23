package com.inventorymanagementsystem.server.service;

import java.util.List;
import java.util.Optional;

import com.inventorymanagementsystem.server.dto.request.AuthenticationRequest;
import com.inventorymanagementsystem.server.dto.response.UserDTO;
import com.inventorymanagementsystem.server.entities.User;

public interface UserService {
    User saveUser(User user);
    Optional<User> getUserById(String id);
    Optional<User> updateUser(User user);
    void deleteUser(String id);
    boolean isUserExistByEmail(String email);

    boolean isUserExistByEmailAndPassword(String email, String password);

    Optional<User> getUserByEmail(String email);
    Optional<User> getUserByUsername(String username);

    Optional<User> updateByUsernameOrEmail(String identifier, User user);

    List<UserDTO> getAllUsers();
    
    void deleteUserByUsernameorEmail(String identifier);

    User register(User user);
    void verify(String email, String otp);
    User login(String email, String password);
    User loginSecurity(AuthenticationRequest authenticationRequest);

    User getCurrentUser();
}
