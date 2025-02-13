package com.inventorymanagementsystem.server.repositories;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.inventorymanagementsystem.server.entities.Contact;

public interface ContactRepo extends JpaRepository<Contact, String> {
    Contact findByEmail(String email);
    List<Contact> findByRepliedFalse();
}
