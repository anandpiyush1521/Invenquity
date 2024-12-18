package com.inventorymanagementsystem.server.entities;

import java.time.LocalDateTime;

import jakarta.persistence.PrePersist;
import jakarta.persistence.PreUpdate;

public class AuditListener {

    @PrePersist
    public void setCreatedAt(Product product) {
        product.setCreatedAt(LocalDateTime.now());
        product.setUpdatedAt(LocalDateTime.now());
    }

    @PreUpdate
    public void setUpdatedAt(Product product) {
        product.setUpdatedAt(LocalDateTime.now());
    }
}
