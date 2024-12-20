package com.inventorymanagementsystem.server.repositories;

import org.springframework.data.jpa.repository.JpaRepository;

import com.inventorymanagementsystem.server.entities.Notification;

public interface NotificationRepo extends JpaRepository<Notification, String> {
    boolean existsById(String id);
}
