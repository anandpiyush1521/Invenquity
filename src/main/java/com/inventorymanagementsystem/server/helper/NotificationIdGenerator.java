package com.inventorymanagementsystem.server.helper;

import java.util.Random;

public class NotificationIdGenerator {

    private static final Random RANDOM = new Random();

    public static String generateNotificationId(String skuCode) {
        int randomNumber = RANDOM.nextInt(10000); // Generates a number between 0 and 9999
        return String.format("NTF-%s-%04d", skuCode, randomNumber); // Combines the SKU code and the formatted number
    }
}
