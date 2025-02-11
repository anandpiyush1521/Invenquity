package com.inventorymanagementsystem.server.controller;

import com.inventorymanagementsystem.server.dto.request.AuthenticationRequest;
import com.inventorymanagementsystem.server.dto.response.AuthenticationRespJwt;
import com.inventorymanagementsystem.server.entities.User;
import com.inventorymanagementsystem.server.service.UserService;
import com.inventorymanagementsystem.server.service.Impl.CustomUserDetailsService;
import com.inventorymanagementsystem.server.util.JwtUtil;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.test.context.support.WithMockUser;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

public class UserControllerTest {

    @InjectMocks
    private UserController userController;

    @Mock
    private UserService userService;

    @Mock
    private CustomUserDetailsService customUserDetailsService;

    @Mock
    private JwtUtil jwtUtil;

    @BeforeEach
    public void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    @WithMockUser(roles = "ADMIN")
    public void testRegisterUser_Success() {
        User user = new User();
        user.setEmail("test@example.com");

        when(userService.isUserExistByEmail(user.getEmail())).thenReturn(false);

        ResponseEntity<?> response = userController.registerUser(user);

        assertEquals(200, response.getStatusCodeValue());
        assertEquals("User registered successfully, please verify your email", response.getBody());
        verify(userService, times(1)).register(user);
    }

    @Test
    @WithMockUser(roles = "ADMIN")
    public void testRegisterUser_EmailAlreadyExists() {
        User user = new User();
        user.setEmail("test@example.com");

        when(userService.isUserExistByEmail(user.getEmail())).thenReturn(true);

        ResponseEntity<?> response = userController.registerUser(user);

        assertEquals(400, response.getStatusCodeValue());
        assertEquals("User with email already exists", response.getBody());
        verify(userService, never()).register(user);
    }

    @Test
    @WithMockUser(roles = "ADMIN")
    public void testVerifyUser_Success() {
        String email = "test@example.com";
        String otp = "123456";

        doNothing().when(userService).verify(email, otp);

        ResponseEntity<?> response = userController.verifyUser(email, otp);

        assertEquals(200, response.getStatusCodeValue());
        assertEquals("User verified successfully", response.getBody());
        verify(userService, times(1)).verify(email, otp);
    }

    @Test
    @WithMockUser(roles = "ADMIN")
    public void testVerifyUser_Failure() {
        String email = "test@example.com";
        String otp = "123456";

        doThrow(new RuntimeException("Invalid OTP")).when(userService).verify(email, otp);

        ResponseEntity<?> response = userController.verifyUser(email, otp);

        assertEquals(400, response.getStatusCodeValue());
        assertEquals("Invalid OTP", response.getBody());
        verify(userService, times(1)).verify(email, otp);
    }

    @Test
    public void testCreateAuthenticationToken_Success() {
        AuthenticationRequest authenticationRequest = new AuthenticationRequest();
        authenticationRequest.setUsername("testuser");
        authenticationRequest.setPassword("password");

        User user = new User();
        user.setUsername("testuser");
        user.setEmail("test@example.com");
        user.setRole("ADMIN");
        user.setFirst_name("Test");
        user.setAddress("123 Main St");

        when(userService.loginSecurity(authenticationRequest)).thenReturn(user);
        UserDetails userDetails = mock(UserDetails.class);
        when(customUserDetailsService.loadUserByUsername(user.getUsername())).thenReturn(userDetails);
        when(jwtUtil.generateToken(anyString(), anyString(), anyString(), anyString(), anyString())).thenReturn("jwt-token");

        ResponseEntity<?> response = userController.createAuthenticationToken(authenticationRequest);

        assertEquals(200, response.getStatusCodeValue());
        assertNotNull(response.getBody());
        assertTrue(response.getBody() instanceof AuthenticationRespJwt);
        assertEquals("jwt-token", ((AuthenticationRespJwt) response.getBody()).getJwt());
    }

    @Test
    public void testCreateAuthenticationToken_Failure() {
        AuthenticationRequest authenticationRequest = new AuthenticationRequest();
        authenticationRequest.setUsername("testuser");
        authenticationRequest.setPassword("password");

        when(userService.loginSecurity(authenticationRequest)).thenThrow(new RuntimeException("Invalid credentials"));

        ResponseEntity<?> response = userController.createAuthenticationToken(authenticationRequest);

        assertEquals(400, response.getStatusCodeValue());
        assertEquals("Invalid credentials", response.getBody());
    }

    @Test
    public void testLogoutUser_Success() {
        String token = "Bearer jwt-token";

        doNothing().when(jwtUtil).invalidateToken("jwt-token");

        ResponseEntity<?> response = userController.logoutUser(token);

        assertEquals(200, response.getStatusCodeValue());
        assertEquals("User logged out successfully", response.getBody());
        verify(jwtUtil, times(1)).invalidateToken("jwt-token");
    }

    @Test
    public void testLogoutUser_Failure() {
        String token = "Bearer jwt-token";

        doThrow(new RuntimeException("Invalid token")).when(jwtUtil).invalidateToken("jwt-token");

        ResponseEntity<?> response = userController.logoutUser(token);

        assertEquals(400, response.getStatusCodeValue());
        assertEquals("Logout failed: Invalid token", response.getBody());
    }

    @Test
    public void testRefreshToken_Success() {
        String token = "Bearer jwt-token";

        when(jwtUtil.refreshToken("jwt-token")).thenReturn("new-jwt-token");

        ResponseEntity<?> response = userController.refreshToken(token);

        assertEquals(200, response.getStatusCodeValue());
        assertNotNull(response.getBody());
        assertTrue(response.getBody() instanceof AuthenticationRespJwt);
        assertEquals("new-jwt-token", ((AuthenticationRespJwt) response.getBody()).getJwt());
    }

    @Test
    public void testRefreshToken_Failure() {
        String token = "Bearer jwt-token";

        when(jwtUtil.refreshToken("jwt-token")).thenThrow(new RuntimeException("Token refresh failed"));

        ResponseEntity<?> response = userController.refreshToken(token);

        assertEquals(400, response.getStatusCodeValue());
        assertEquals("Token refresh failed: Token refresh failed", response.getBody());
    }
}