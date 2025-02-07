package com.inventorymanagementsystem.server.controller;


import com.inventorymanagementsystem.server.entities.Subscription;
import com.inventorymanagementsystem.server.entities.User;
import com.inventorymanagementsystem.server.service.SubscriptionService;
import com.inventorymanagementsystem.server.service.UserService;
import com.stripe.exception.StripeException;
import com.stripe.model.checkout.Session;
import com.stripe.param.checkout.SessionCreateParams;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

@RestController
@RequestMapping("/api/invenquity/subscription")
public class SubscriptionController {

    private static final Logger logger = LoggerFactory.getLogger(SubscriptionController.class);

    @Autowired
    private SubscriptionService subscriptionService;

    @Autowired
    private UserService userService;

    @PostMapping("/create-checkout-session")
    public ResponseEntity<Map<String, Object>> createCheckoutSession(@RequestBody Map<String, String> requestBody) throws StripeException {
        String customerEmail = requestBody.get("email");
        if (customerEmail == null || customerEmail.isEmpty()) {
            throw new IllegalArgumentException("Email is required");
        }

        if (subscriptionService.isEmailSubscribed(customerEmail)) {
            throw new IllegalArgumentException("Email is already subscribed");
        }

        SessionCreateParams params = SessionCreateParams.builder()
            .addPaymentMethodType(SessionCreateParams.PaymentMethodType.CARD)
            .setCustomerEmail(customerEmail)
            .setMode(SessionCreateParams.Mode.PAYMENT)
            .setSuccessUrl("http://localhost:8080/api/invenquity/subscription/success?session_id={CHECKOUT_SESSION_ID}")
            .setCancelUrl("http://localhost:8080/api/invenquity/subscription/cancel")
            .addLineItem(
                SessionCreateParams.LineItem.builder()
                    .setPriceData(
                        SessionCreateParams.LineItem.PriceData.builder()
                            .setCurrency("inr")
                            .setUnitAmount(100L)
                            .setProductData(
                                SessionCreateParams.LineItem.PriceData.ProductData.builder()
                                    .setName("Test Product")
                                    .build()
                            )
                            .build()
                    )
                    .setQuantity(1L)
                    .build()
            )
            .setShippingAddressCollection(
                SessionCreateParams.ShippingAddressCollection.builder()
                    .addAllowedCountry(SessionCreateParams.ShippingAddressCollection.AllowedCountry.IN)
                    .build()
            )
            .putMetadata("customer_name", "John Doe")
            .putMetadata("customer_address", "123, Main Street, New Delhi, India")
            .build();

        Session session = Session.create(params);
        Map<String, Object> result = new HashMap<>();
        result.put("sessionId", session.getId());
        return ResponseEntity.ok(result);
    }

    @GetMapping("/success")
    public String getSuccess(@RequestParam("session_id") String sessionId) throws StripeException {
        logger.info("Payment successful for session_id: {}", sessionId);
        Session session = Session.retrieve(sessionId);
        String customerEmail = session.getCustomerEmail();

        // Retrieve the payment ID from the session
        String paymentId = session.getPaymentIntent(); // Assuming the payment ID is available as paymentIntent
        logger.info("Retrieved payment ID: {}", paymentId);

        // Save subscription details
        Subscription subscription = Subscription.builder()
            .email(customerEmail)
            .country("India")
            .amount(new BigDecimal("60"))
            .stripeCustomerId(session.getCustomer())
            .paymentId(paymentId) // Set the payment ID
            .build();

        subscriptionService.saveSubscription(subscription);

        // Save user details
        User user = User.builder()
            .email(customerEmail)
            .password("ADMIN@123456789")
            .repeat_password("ADMIN@123456789")
            .first_name("admin")
            .last_name("admin")
            .phone("1234567890")
            .address("123, Main Street, New Delhi, India")
            .otp("999999")
            .isEmailVerified(true)
            .role("ADMIN")
            .localDateTime(LocalDateTime.now())
            .build();

        userService.saveSubscribeUser(user);

        return "Payment successful";
    }

    @GetMapping("/cancel")
    public String cancel() {
        return "Payment canceled";
    }
}
