package com.inventorymanagementsystem.server.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.inventorymanagementsystem.server.entities.Notification;
import com.inventorymanagementsystem.server.service.NotificationService;

@RestController
@RequestMapping("/api/invenquity/notifications")
public class NotificationController {

    @Autowired
    private NotificationService notificationService;

    @GetMapping
    public ResponseEntity<List<Notification>> getAllNotificationsSortedByDateTime() {
        try {
            List<Notification> notifications = notificationService.getAllNotificationsSortedByDateTime();
            return ResponseEntity.ok(notifications);
        } catch (Exception e) {
            return ResponseEntity.status(500).body(null);
        }
    }
}
