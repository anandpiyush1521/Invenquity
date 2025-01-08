package com.inventorymanagementsystem.server.service.Impl;

import java.time.LocalDateTime;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.inventorymanagementsystem.server.entities.Contact;
import com.inventorymanagementsystem.server.helper.ContactMessageIdGenerator;
import com.inventorymanagementsystem.server.repositories.ContactRepo;
import com.inventorymanagementsystem.server.service.ContactService;

@Service
public class ContactServiceImpl implements ContactService{

    @Autowired
    private ContactRepo contactRepo;

    @Override
    public Contact saveContactMessage(Contact contact) {
        String generatedId;
        do {
            generatedId = ContactMessageIdGenerator.generateContactMessageId();
        } while (contactRepo.existsById(generatedId));
        contact.setId(generatedId);
        contact.setCreatedAt(LocalDateTime.now());

        return contactRepo.save(contact);
    }

    @Override
    public List<Contact> getAllContactMessages() {
        return contactRepo.findAll();
    }

}
