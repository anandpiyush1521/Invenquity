package com.inventorymanagementsystem.server.service;

import java.util.List;

import com.inventorymanagementsystem.server.entities.Subscription;

public interface SubscriptionService {
    boolean isEmailSubscribed(String email);
    Subscription saveSubscription(Subscription subscription);
    List<Subscription> getAllSubscriptions();
}
