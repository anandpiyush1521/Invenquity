package com.inventorymanagementsystem.server.service;

import java.util.List;

import com.inventorymanagementsystem.server.entities.Contact;

public interface ContactService {
    Contact saveContactMessage(Contact contact);
    List<Contact> getAllContactMessages();
    void sendReplyToContact(String email, String replyMessage);
    List<Contact> getUnrepliedContactMessages();
}
