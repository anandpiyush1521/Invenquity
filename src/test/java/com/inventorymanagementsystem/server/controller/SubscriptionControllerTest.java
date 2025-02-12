package com.inventorymanagementsystem.server.controller;

import com.inventorymanagementsystem.server.entities.Subscription;
import com.inventorymanagementsystem.server.entities.User;
import com.inventorymanagementsystem.server.service.SubscriptionService;
import com.inventorymanagementsystem.server.service.UserService;
import com.stripe.exception.StripeException;
import com.stripe.model.Customer;
import com.stripe.model.checkout.Session;
import com.stripe.param.checkout.SessionCreateParams;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.http.ResponseEntity;
import org.springframework.mail.javamail.JavaMailSender;

import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

public class SubscriptionControllerTest {

    @InjectMocks
    private SubscriptionController subscriptionController;

    @Mock
    private SubscriptionService subscriptionService;

    @Mock
    private UserService userService;

    @Mock
    private JavaMailSender mailSender;

    @BeforeEach
    public void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    public void testCreateCheckoutSession_Success() throws StripeException {
        Map<String, String> requestBody = new HashMap<>();
        requestBody.put("email", "test@example.com");

        when(subscriptionService.isEmailSubscribed("test@example.com")).thenReturn(false);

        SessionCreateParams params = SessionCreateParams.builder()
                .addPaymentMethodType(SessionCreateParams.PaymentMethodType.CARD)
                .addPaymentMethodType(SessionCreateParams.PaymentMethodType.ALIPAY)
                .setCustomerEmail("test@example.com")
                .setMode(SessionCreateParams.Mode.PAYMENT)
                .setSuccessUrl("http://localhost:8080/api/invenquity/subscription/success?session_id={CHECKOUT_SESSION_ID}")
                .setCancelUrl("http://localhost:8080/api/invenquity/subscription/cancel")
                .addLineItem(
                        SessionCreateParams.LineItem.builder()
                                .setPriceData(
                                        SessionCreateParams.LineItem.PriceData.builder()
                                                .setCurrency("inr")
                                                .setUnitAmount(6000L)
                                                .setProductData(
                                                        SessionCreateParams.LineItem.PriceData.ProductData.builder()
                                                                .setName("Test Product")
                                                                .build())
                                                .build())
                                .setQuantity(1L)
                                .build())
                .setShippingAddressCollection(
                        SessionCreateParams.ShippingAddressCollection.builder()
                                .addAllowedCountry(SessionCreateParams.ShippingAddressCollection.AllowedCountry.IN)
                                .build())
                .putMetadata("customer_name", "John Doe")
                .putMetadata("customer_address", "123, Main Street, New Delhi, India")
                .build();

        Session session = mock(Session.class);
        when(session.getId()).thenReturn("session_id");
        mockStatic(Session.class);
        when(Session.create(any(SessionCreateParams.class))).thenReturn(session);

        ResponseEntity<Map<String, Object>> response = subscriptionController.createCheckoutSession(requestBody);

        assertEquals(200, response.getStatusCodeValue());
        assertNotNull(response.getBody());
        assertEquals("session_id", response.getBody().get("sessionId"));
        verify(subscriptionService, times(1)).isEmailSubscribed("test@example.com");
    }

    @Test
    public void testCreateCheckoutSession_EmailAlreadySubscribed() {
        Map<String, String> requestBody = new HashMap<>();
        requestBody.put("email", "test@example.com");

        when(subscriptionService.isEmailSubscribed("test@example.com")).thenReturn(true);

        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class, () -> {
            subscriptionController.createCheckoutSession(requestBody);
        });

        assertEquals("Email is already subscribed", exception.getMessage());
        verify(subscriptionService, times(1)).isEmailSubscribed("test@example.com");
    }

    @Test
    public void testCreateCheckoutSession_EmailRequired() {
        Map<String, String> requestBody = new HashMap<>();

        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class, () -> {
            subscriptionController.createCheckoutSession(requestBody);
        });

        assertEquals("Email is required", exception.getMessage());
    }

    @Test
    public void testGetSuccess_Success() throws StripeException {
        String sessionId = "session_id";
        Session session = mock(Session.class);
        when(session.getCustomerEmail()).thenReturn("test@example.com");
        when(session.getPaymentIntent()).thenReturn("payment_id");
        when(session.getShipping().getAddress().getLine1()).thenReturn("123");
        when(session.getShipping().getAddress().getLine2()).thenReturn("Main Street");
        when(session.getShipping().getAddress().getCity()).thenReturn("New Delhi");
        when(session.getShipping().getAddress().getPostalCode()).thenReturn("110001");
        when(session.getShipping().getAddress().getState()).thenReturn("Delhi");
        when(session.getCustomer()).thenReturn("customer_id");
        mockStatic(Session.class);
        when(Session.retrieve(sessionId)).thenReturn(session);

        Customer customer = mock(Customer.class);
        when(customer.getName()).thenReturn("John Doe");
        mockStatic(Customer.class);
        when(Customer.retrieve("customer_id")).thenReturn(customer);

        ResponseEntity<Void> response = subscriptionController.getSuccess(sessionId);

        assertEquals(302, response.getStatusCodeValue());
        assertEquals("http://localhost:3000/admin-login", response.getHeaders().getLocation().toString());
        verify(subscriptionService, times(1)).saveSubscription(any(Subscription.class));
        verify(userService, times(1)).saveSubscribeUser(any(User.class));
    }

    @Test
    public void testFetchAllSubscriptionInfo_Success() {
        List<Subscription> subscriptions = Arrays.asList(new Subscription(), new Subscription());
        when(subscriptionService.getAllSubscriptions()).thenReturn(subscriptions);

        ResponseEntity<?> response = subscriptionController.fetchAllSubscriptionInfo();

        assertEquals(200, response.getStatusCodeValue());
        assertEquals(subscriptions, response.getBody());
        verify(subscriptionService, times(1)).getAllSubscriptions();
    }
}