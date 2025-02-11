package com.inventorymanagementsystem.server.controller;

import com.inventorymanagementsystem.server.dto.response.UserDTO;
import com.inventorymanagementsystem.server.entities.User;
import com.inventorymanagementsystem.server.service.UserService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.http.ResponseEntity;
import org.springframework.security.test.context.support.WithMockUser;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

public class UserProfileControllerTest {

    @InjectMocks
    private UserProfileController userProfileController;

    @Mock
    private UserService userService;

    @BeforeEach
    public void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    @WithMockUser(roles = "ADMIN")
    public void testFetchAllUsers_Success() {
        List<UserDTO> userDTOs = Arrays.asList(new UserDTO(new User()), new UserDTO(new User()));
        when(userService.getAllUsers()).thenReturn(userDTOs);

        ResponseEntity<?> response = userProfileController.fetchAllUsers();

        assertEquals(200, response.getStatusCodeValue());
        assertEquals(userDTOs, response.getBody());
        verify(userService, times(1)).getAllUsers();
    }

    @Test
    @WithMockUser(roles = "ADMIN")
    public void testFetchAllUsers_Failure() {
        when(userService.getAllUsers()).thenThrow(new RuntimeException("Error fetching users"));

        ResponseEntity<?> response = userProfileController.fetchAllUsers();

        assertEquals(500, response.getStatusCodeValue());
        assertEquals("An error occurred while fetching all users", response.getBody());
        verify(userService, times(1)).getAllUsers();
    }

    @Test
    @WithMockUser(roles = "ADMIN")
    public void testFetchUserProfile_Success() {
        User user = new User();
        when(userService.getUserById("1")).thenReturn(Optional.of(user));

        ResponseEntity<?> response = userProfileController.fetchUserProfile("1");

        assertEquals(200, response.getStatusCodeValue());
        assertEquals(new UserDTO(user), response.getBody());
        verify(userService, times(1)).getUserById("1");
    }

    @Test
    @WithMockUser(roles = "ADMIN")
    public void testFetchUserProfile_NotFound() {
        when(userService.getUserById("1")).thenReturn(Optional.empty());

        ResponseEntity<?> response = userProfileController.fetchUserProfile("1");

        assertEquals(404, response.getStatusCodeValue());
        assertEquals("User not found", response.getBody());
        verify(userService, times(1)).getUserById("1");
    }

    @Test
    @WithMockUser(roles = "ADMIN")
    public void testFetchUserProfileByUsername_Success() {
        User user = new User();
        when(userService.getUserByUsername("username")).thenReturn(Optional.of(user));

        ResponseEntity<?> response = userProfileController.fetchUserProfileByUsername("username");

        assertEquals(200, response.getStatusCodeValue());
        assertEquals(new UserDTO(user), response.getBody());
        verify(userService, times(1)).getUserByUsername("username");
    }

    @Test
    @WithMockUser(roles = "ADMIN")
    public void testFetchUserProfileByUsername_NotFound() {
        when(userService.getUserByUsername("username")).thenReturn(Optional.empty());

        ResponseEntity<?> response = userProfileController.fetchUserProfileByUsername("username");

        assertEquals(404, response.getStatusCodeValue());
        assertEquals("User not found", response.getBody());
        verify(userService, times(1)).getUserByUsername("username");
    }

    @Test
    @WithMockUser(roles = "ADMIN")
    public void testFetchUserProfileByEmail_Success() {
        User user = new User();
        when(userService.getUserByEmail("email@example.com")).thenReturn(Optional.of(user));

        ResponseEntity<?> response = userProfileController.fetchUserProfileByEmail("email@example.com");

        assertEquals(200, response.getStatusCodeValue());
        assertEquals(new UserDTO(user), response.getBody());
        verify(userService, times(1)).getUserByEmail("email@example.com");
    }

    @Test
    @WithMockUser(roles = "ADMIN")
    public void testFetchUserProfileByEmail_NotFound() {
        when(userService.getUserByEmail("email@example.com")).thenReturn(Optional.empty());

        ResponseEntity<?> response = userProfileController.fetchUserProfileByEmail("email@example.com");

        assertEquals(404, response.getStatusCodeValue());
        assertEquals("User not found", response.getBody());
        verify(userService, times(1)).getUserByEmail("email@example.com");
    }

    @Test
    @WithMockUser(roles = "ADMIN")
    public void testUpdateUserProfile_Success() {
        User user = new User();
        when(userService.updateUser(user)).thenReturn(Optional.of(user));

        ResponseEntity<?> response = userProfileController.updateUserProfile("1", user);

        assertEquals(200, response.getStatusCodeValue());
        assertEquals(user, response.getBody());
        verify(userService, times(1)).updateUser(user);
    }

    @Test
    @WithMockUser(roles = "ADMIN")
    public void testUpdateUserProfile_NotFound() {
        User user = new User();
        when(userService.updateUser(user)).thenThrow(new RuntimeException("User not found"));

        ResponseEntity<?> response = userProfileController.updateUserProfile("1", user);

        assertEquals(404, response.getStatusCodeValue());
        assertEquals("User not found", response.getBody());
        verify(userService, times(1)).updateUser(user);
    }

    @Test
    @WithMockUser(roles = "ADMIN")
    public void testDeleteUserProfile_Success() {
        doNothing().when(userService).deleteUser("1");

        ResponseEntity<?> response = userProfileController.deleteUserProfile("1");

        assertEquals(200, response.getStatusCodeValue());
        assertEquals("User deleted successfully", response.getBody());
        verify(userService, times(1)).deleteUser("1");
    }

    @Test
    @WithMockUser(roles = "ADMIN")
    public void testDeleteUserProfile_NotFound() {
        doThrow(new RuntimeException("User not found")).when(userService).deleteUser("1");

        ResponseEntity<?> response = userProfileController.deleteUserProfile("1");

        assertEquals(404, response.getStatusCodeValue());
        assertEquals("User not found", response.getBody());
        verify(userService, times(1)).deleteUser("1");
    }
}