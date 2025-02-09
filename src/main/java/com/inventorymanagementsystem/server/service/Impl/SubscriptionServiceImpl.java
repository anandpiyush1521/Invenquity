package com.inventorymanagementsystem.server.service.Impl;

import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.inventorymanagementsystem.server.entities.Subscription;
import com.inventorymanagementsystem.server.helper.SubscriptionIdGenerator;
import com.inventorymanagementsystem.server.repositories.SubscriptionRepo;
import com.inventorymanagementsystem.server.service.SubscriptionService;

@Service
public class SubscriptionServiceImpl implements SubscriptionService {

    @Autowired
    private SubscriptionRepo subscriptionRepo;

    private static final Logger logger = LoggerFactory.getLogger(SubscriptionService.class);

    @Override
    public boolean isEmailSubscribed(String email) {
        return subscriptionRepo.existsByEmail(email);
    }

    @Override
    public Subscription saveSubscription(Subscription subscription) {
        logger.info("Saving subscription for email: {}", subscription.getEmail());
        String subscribeId;
        do {
            subscribeId = SubscriptionIdGenerator.generateSubscriptionId();
        } while (subscriptionRepo.existsById(subscribeId));
        subscription.setId(subscribeId);
        return subscriptionRepo.save(subscription);
    }

    @Override
    public List<Subscription> getAllSubscriptions() {
        return subscriptionRepo.findAll();
    }

}
