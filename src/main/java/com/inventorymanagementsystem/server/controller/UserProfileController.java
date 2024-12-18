package com.inventorymanagementsystem.server.controller;

import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.inventorymanagementsystem.server.entities.User;
import com.inventorymanagementsystem.server.service.UserService;

@RestController
@RequestMapping("/api/invenquity/user")
public class UserProfileController {

    @Autowired
    private UserService userService;

    @GetMapping
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<?> fetchAllUsers() {
        try {
            return ResponseEntity.ok(userService.getAllUsers());
        } catch (Exception e) {
            return ResponseEntity.status(500).body("An error occurred while fetching all users");
        }
    }

    // Fetch User Profile (Admin only)
    @GetMapping("/id/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<?> fetchUserProfile(@PathVariable String id) {
        try {
            return userService.getUserById(id)
                    .map(ResponseEntity::ok)
                    .orElseThrow(() -> new RuntimeException("User not found"));
        } catch (RuntimeException e) {
            return ResponseEntity.status(404).body(e.getMessage());
        } catch (Exception e) {
            return ResponseEntity.status(500).body("An error occurred while fetching the user profile");
        }
    }

    @GetMapping("/username/{username}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<?> fetchUserProfileByUsername(@PathVariable String username) {
        try {
            return userService.getUserByUsername(username)
                    .map(ResponseEntity::ok)
                    .orElseThrow(() -> new RuntimeException("User not found"));
        } catch (RuntimeException e) {
            return ResponseEntity.status(404).body(e.getMessage());
        } catch(Exception e) {
            return ResponseEntity.status(500).body("An error occurred while fetching the user profile");
        }
    }

    @GetMapping("/email/{email}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<?> fetchUserProfileByEmail(@PathVariable String email) {
        try {
            return userService.getUserByEmail(email)
                    .map(ResponseEntity::ok)
                    .orElseThrow(() -> new RuntimeException("User not found"));
        } catch (RuntimeException e) {
            return ResponseEntity.status(404).body(e.getMessage());
        } catch(Exception e) {
            return ResponseEntity.status(500).body("An error occurred while fetching the user profile");
        }
    }

    // Update User Profile (Admin only)
    @PutMapping("/id/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<?> updateUserProfile(@PathVariable String id, @RequestBody User user) {
        try {
            user.setId(id);
            return ResponseEntity.ok(userService.updateUser(user));
        } catch (RuntimeException e) {
            return ResponseEntity.status(404).body(e.getMessage());
        } catch (Exception e) {
            return ResponseEntity.status(500).body("An error occurred while updating the user profile");
        }
    }

    @PutMapping("/update/{identifier}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<?> updateUserByUsernameOrEmail(@PathVariable String identifier, @RequestBody User user) {
        Optional<User> updatedUser = userService.updateByUsernameOrEmail(identifier, user);

        if(updatedUser.isPresent()) {
            return ResponseEntity.ok(updatedUser.get());
        } else {
            return ResponseEntity.status(404).body("User not found");
        }
    }

    // Delete User Profile (Admin only)
    @DeleteMapping("/id/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<?> deleteUserProfile(@PathVariable String id) {
        try {
            userService.deleteUser(id);
            return ResponseEntity.ok("User deleted successfully");
        } catch (RuntimeException e) {
            return ResponseEntity.status(404).body(e.getMessage());
        } catch (Exception e) {
            return ResponseEntity.status(500).body("An error occurred while deleting the user profile");
        }
    }

    @DeleteMapping("/delete/{identifier}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<?> deleteUserByUsernameOrEmail(@PathVariable String identifier) {
        try {
            userService.deleteUserByUsernameorEmail(identifier);
            return ResponseEntity.ok("User deleted successfully");
        } catch (RuntimeException e) {
            return ResponseEntity.status(404).body(e.getMessage());
        } catch (Exception e) {
            return ResponseEntity.status(500).body("An error occurred while deleting the user profile");
        }
    }
}