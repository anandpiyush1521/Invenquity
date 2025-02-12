package com.inventorymanagementsystem.server.controller;

import com.inventorymanagementsystem.server.entities.ForgotPassword;
import com.inventorymanagementsystem.server.entities.User;
import com.inventorymanagementsystem.server.helper.PasswordBcrypt;
import com.inventorymanagementsystem.server.repositories.ForgotPasswordRepo;
import com.inventorymanagementsystem.server.repositories.UserRepo;
import com.inventorymanagementsystem.server.service.Impl.EmailService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.http.ResponseEntity;

import java.time.LocalDateTime;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

public class ForgotPasswordControllerTest {

    @InjectMocks
    private ForgotPasswordController forgotPasswordController;

    @Mock
    private UserRepo userRepo;

    @Mock
    private ForgotPasswordRepo forgotPasswordRepo;

    @Mock
    private EmailService emailService;

    @BeforeEach
    public void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    public void testRequestOtp_UserNotFound() {
        User user = new User();
        user.setEmail("test@example.com");

        when(userRepo.findByEmail(user.getEmail())).thenReturn(Optional.empty());

        ResponseEntity<String> response = forgotPasswordController.requestOtp(user);

        assertEquals(400, response.getStatusCodeValue());
        assertEquals("User with email does not exist", response.getBody());
        verify(userRepo, times(1)).findByEmail(user.getEmail());
    }

    @Test
    public void testRequestOtp_Success() {
        User user = new User();
        user.setEmail("test@example.com");
        user.setFirst_name("John");

        ForgotPassword forgotPassword = new ForgotPassword();
        forgotPassword.setUser(user);

        when(userRepo.findByEmail(user.getEmail())).thenReturn(Optional.of(user));
        when(forgotPasswordRepo.findByUser_Email(user.getEmail())).thenReturn(Optional.of(forgotPassword));

        ResponseEntity<String> response = forgotPasswordController.requestOtp(user);

        assertEquals(200, response.getStatusCodeValue());
        assertEquals("OTP sent successfully to Your Email", response.getBody());
        verify(userRepo, times(1)).findByEmail(user.getEmail());
        verify(forgotPasswordRepo, times(1)).findByUser_Email(user.getEmail());
        verify(forgotPasswordRepo, times(1)).save(any(ForgotPassword.class));
        verify(emailService, times(1)).sendEmail(eq(user.getEmail()), anyString(), anyString());
    }

    @Test
    public void testVerifyOtp_InvalidRequest() {
        ForgotPassword forgotPasswordRequest = new ForgotPassword();
        User user = new User();
        user.setEmail("test@example.com");
        forgotPasswordRequest.setUser(user);

        when(forgotPasswordRepo.findByUser_Email(user.getEmail())).thenReturn(Optional.empty());

        ResponseEntity<?> response = forgotPasswordController.verifyOtp(forgotPasswordRequest);

        assertEquals(400, response.getStatusCodeValue());
        assertEquals("Invalid request.", response.getBody());
        verify(forgotPasswordRepo, times(1)).findByUser_Email(user.getEmail());
    }

    @Test
    public void testVerifyOtp_InvalidOrExpiredOtp() {
        ForgotPassword forgotPasswordRequest = new ForgotPassword();
        User user = new User();
        user.setEmail("test@example.com");
        forgotPasswordRequest.setUser(user);
        forgotPasswordRequest.setOtp("123456");

        ForgotPassword forgotPassword = new ForgotPassword();
        forgotPassword.setUser(user);
        forgotPassword.setOtp("654321");
        forgotPassword.setExpirationTime(LocalDateTime.now().minusMinutes(1));

        when(forgotPasswordRepo.findByUser_Email(user.getEmail())).thenReturn(Optional.of(forgotPassword));

        ResponseEntity<?> response = forgotPasswordController.verifyOtp(forgotPasswordRequest);

        assertEquals(400, response.getStatusCodeValue());
        assertEquals("Invalid or expired OTP.", response.getBody());
        verify(forgotPasswordRepo, times(1)).findByUser_Email(user.getEmail());
    }

    @Test
    public void testVerifyOtp_Success() {
        ForgotPassword forgotPasswordRequest = new ForgotPassword();
        User user = new User();
        user.setEmail("test@example.com");
        user.setPassword("newPassword");
        forgotPasswordRequest.setUser(user);
        forgotPasswordRequest.setOtp("123456");

        ForgotPassword forgotPassword = new ForgotPassword();
        forgotPassword.setUser(user);
        forgotPassword.setOtp("123456");
        forgotPassword.setExpirationTime(LocalDateTime.now().plusMinutes(5));

        when(forgotPasswordRepo.findByUser_Email(user.getEmail())).thenReturn(Optional.of(forgotPassword));

        ResponseEntity<?> response = forgotPasswordController.verifyOtp(forgotPasswordRequest);

        assertEquals(200, response.getStatusCodeValue());
        assertEquals("Password reset successfully.", response.getBody());
        verify(forgotPasswordRepo, times(1)).findByUser_Email(user.getEmail());
        verify(userRepo, times(1)).save(any(User.class));
        verify(forgotPasswordRepo, times(1)).delete(forgotPassword);
    }
}