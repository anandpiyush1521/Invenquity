package com.inventorymanagementsystem.server.service;

import java.util.List;

import com.inventorymanagementsystem.server.entities.Notification;

public interface NotificationService {
    List<Notification> getAllNotificationsSortedByDateTime();
}
