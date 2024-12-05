package com.inventorymanagementsystem.server.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import com.inventorymanagementsystem.server.entities.ForgotPassword;

import java.util.Optional;

public interface ForgotPasswordRepo extends JpaRepository<ForgotPassword, Integer> {
    Optional<ForgotPassword> findByUser_Email(String email);
}
