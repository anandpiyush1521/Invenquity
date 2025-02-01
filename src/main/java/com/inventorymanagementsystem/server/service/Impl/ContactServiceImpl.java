package com.inventorymanagementsystem.server.service.Impl;

import java.time.LocalDateTime;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.inventorymanagementsystem.server.entities.Contact;
import com.inventorymanagementsystem.server.helper.ContactMessageIdGenerator;
import com.inventorymanagementsystem.server.helper.EmailTemplate;
import com.inventorymanagementsystem.server.repositories.ContactRepo;
import com.inventorymanagementsystem.server.service.ContactService;
import com.inventorymanagementsystem.server.util.JwtUtil;

@Service
public class ContactServiceImpl implements ContactService{

    @Autowired
    private ContactRepo contactRepo;

    @Autowired
    private JwtUtil jwtUtil;

    @Autowired
    private EmailService emailService;

    private static final Logger logger = LoggerFactory.getLogger(ContactServiceImpl.class);

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

    @Override
    public void sendReplyToContact(String email, String replyMessage) {
        String userEmail = "piyushanand2580@gmail.com"; //Admin user email
        logger.info("Admin User Email: " + userEmail);

        Contact contact = contactRepo.findByEmail(email); // Get contact by provided email
        if (contact != null) {
            String subject = "Reply to: " + contact.getSubject();
            String message = EmailTemplate.getEmailTemplateForReply(contact.getFirstName(), contact.getLastName(), subject, replyMessage, userEmail);
            emailService.sendEmail(contact.getEmail(), subject, message);
            logger.info("Reply sent to: " + contact.getEmail());
        } else {
            logger.warn("No contact found with email: " + email);
        }
    }
}
