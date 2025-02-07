package com.inventorymanagementsystem.server.entities;

import java.math.BigDecimal;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Entity
@Table(name = "subscription")
public class Subscription {
    @Id
    private String id;

    private String email;
    private String country;
    private BigDecimal amount;
    private String stripeCustomerId;
    private String paymentId; // Replace stripeSubscriptionId with paymentId
}
