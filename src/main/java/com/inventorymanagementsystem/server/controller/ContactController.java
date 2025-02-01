package com.inventorymanagementsystem.server.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.inventorymanagementsystem.server.dto.request.ReplyRequest;
import com.inventorymanagementsystem.server.entities.Contact;
import com.inventorymanagementsystem.server.service.ContactService;

@RestController
@RequestMapping("/api/invenquity/contact")
public class ContactController {

    @Autowired
    private ContactService contactService;

    @PostMapping("/save")
    public ResponseEntity<?> saveContactMessage(@RequestBody Contact contact) {
        return ResponseEntity.ok(contactService.saveContactMessage(contact));
    }

    @GetMapping("/all")
    public ResponseEntity<?> getAllContactMessages() {
        return ResponseEntity.ok(contactService.getAllContactMessages());
    }

    @PostMapping("/reply")
    public ResponseEntity<?> sendReplyToContact(
        @RequestHeader("Authorization") String jwtToken,
        @RequestBody ReplyRequest replyRequest) {
    
        contactService.sendReplyToContact(replyRequest.getEmail(), replyRequest.getMessage());
        return ResponseEntity.ok("Reply sent successfully.");
    }
}
