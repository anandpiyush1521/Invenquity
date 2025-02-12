package com.inventorymanagementsystem.server.controller;

import com.inventorymanagementsystem.server.entities.Notification;
import com.inventorymanagementsystem.server.service.NotificationService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.http.ResponseEntity;

import java.util.Arrays;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

public class NotificationControllerTest {

    @InjectMocks
    private NotificationController notificationController;

    @Mock
    private NotificationService notificationService;

    @BeforeEach
    public void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    public void testGetAllNotificationsSortedByDateTime_Success() {
        List<Notification> notifications = Arrays.asList(new Notification(), new Notification());
        when(notificationService.getAllNotificationsSortedByDateTime()).thenReturn(notifications);

        ResponseEntity<List<Notification>> response = notificationController.getAllNotificationsSortedByDateTime();

        assertEquals(200, response.getStatusCodeValue());
        assertEquals(notifications, response.getBody());
        verify(notificationService, times(1)).getAllNotificationsSortedByDateTime();
    }

    @Test
    public void testGetAllNotificationsSortedByDateTime_Failure() {
        when(notificationService.getAllNotificationsSortedByDateTime()).thenThrow(new RuntimeException("Error fetching notifications"));

        ResponseEntity<List<Notification>> response = notificationController.getAllNotificationsSortedByDateTime();

        assertEquals(500, response.getStatusCodeValue());
        assertNull(response.getBody());
        verify(notificationService, times(1)).getAllNotificationsSortedByDateTime();
    }
}