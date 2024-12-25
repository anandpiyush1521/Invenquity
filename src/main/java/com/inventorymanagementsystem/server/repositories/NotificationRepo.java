package com.inventorymanagementsystem.server.repositories;

import java.util.List;

import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.repository.JpaRepository;

import com.inventorymanagementsystem.server.entities.Notification;

public interface NotificationRepo extends JpaRepository<Notification, String> {
    boolean existsById(String id);
    List<Notification> findAll(Sort sort);
}
