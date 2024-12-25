package com.inventorymanagementsystem.server.service.Impl;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import com.inventorymanagementsystem.server.entities.Notification;
import com.inventorymanagementsystem.server.repositories.NotificationRepo;
import com.inventorymanagementsystem.server.service.NotificationService;

@Service
public class NotificationServiceImpl implements NotificationService {

    @Autowired
    private NotificationRepo notificationRepo;

    // public NotificationServiceImpl(NotificationRepo notificationRepo) {
    //     this.notificationRepo = notificationRepo;
    // }

    @Override
    public List<Notification> getAllNotificationsSortedByDateTime() {
        return notificationRepo.findAll(Sort.by(Sort.Direction.DESC, "createdAt"));
    }


}
