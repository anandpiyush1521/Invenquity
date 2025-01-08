package com.inventorymanagementsystem.server.entities;

import java.time.LocalDateTime;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Table(name = "contact")
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class Contact {
    @Id
    private String id;

    private String firstName;
    private String lastName;
    private String email;

    @Column(columnDefinition = "TEXT")
    private String subject;

    @Column(columnDefinition = "TEXT")
    private String message;

    private LocalDateTime createdAt;
}
