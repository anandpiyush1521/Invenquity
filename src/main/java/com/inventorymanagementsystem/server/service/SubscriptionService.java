package com.inventorymanagementsystem.server.service;

import com.inventorymanagementsystem.server.entities.Subscription;

public interface SubscriptionService {
    boolean isEmailSubscribed(String email);
    Subscription saveSubscription(Subscription subscription);
}
