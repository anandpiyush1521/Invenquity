package com.inventorymanagementsystem.server.controller;

import com.inventorymanagementsystem.server.dto.request.ReplyRequest;
import com.inventorymanagementsystem.server.entities.Contact;
import com.inventorymanagementsystem.server.service.ContactService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.http.ResponseEntity;

import java.util.Arrays;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

public class ContactControllerTest {

    @InjectMocks
    private ContactController contactController;

    @Mock
    private ContactService contactService;

    @BeforeEach
    public void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    public void testSaveContactMessage_Success() {
        Contact contact = new Contact();
        when(contactService.saveContactMessage(contact)).thenReturn(contact);

        ResponseEntity<?> response = contactController.saveContactMessage(contact);

        assertEquals(200, response.getStatusCodeValue());
        assertEquals(contact, response.getBody());
        verify(contactService, times(1)).saveContactMessage(contact);
    }

    @Test
    public void testGetAllContactMessages_Success() {
        List<Contact> contacts = Arrays.asList(new Contact(), new Contact());
        when(contactService.getAllContactMessages()).thenReturn(contacts);

        ResponseEntity<?> response = contactController.getAllContactMessages();

        assertEquals(200, response.getStatusCodeValue());
        assertEquals(contacts, response.getBody());
        verify(contactService, times(1)).getAllContactMessages();
    }

    @Test
    public void testSendReplyToContact_Success() {
        ReplyRequest replyRequest = new ReplyRequest();
        replyRequest.setEmail("test@example.com");
        replyRequest.setMessage("Test reply message");

        doNothing().when(contactService).sendReplyToContact(replyRequest.getEmail(), replyRequest.getMessage());

        ResponseEntity<?> response = contactController.sendReplyToContact("Bearer jwtToken", replyRequest);

        assertEquals(200, response.getStatusCodeValue());
        assertEquals("Reply sent successfully.", response.getBody());
        verify(contactService, times(1)).sendReplyToContact(replyRequest.getEmail(), replyRequest.getMessage());
    }
}