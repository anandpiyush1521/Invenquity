package com.inventorymanagementsystem.server.repositories;

import org.springframework.data.jpa.repository.JpaRepository;

import com.inventorymanagementsystem.server.entities.Contact;

public interface ContactRepo extends JpaRepository<Contact, String> {
    Contact findByEmail(String email);
}
