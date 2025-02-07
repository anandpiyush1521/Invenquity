package com.inventorymanagementsystem.server.repositories;

import org.springframework.data.jpa.repository.JpaRepository;

import com.inventorymanagementsystem.server.entities.Subscription;

public interface SubscriptionRepo extends JpaRepository<Subscription, String> {
    boolean existsByEmail(String email);
}
