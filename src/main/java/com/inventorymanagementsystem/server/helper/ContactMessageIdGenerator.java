package com.inventorymanagementsystem.server.helper;

import java.util.Random;

public class ContactMessageIdGenerator {
    private static final Random RANDOM = new Random();

    public static String generateContactMessageId() {
        int randomNumber = RANDOM.nextInt(100000); // Generates a number between 0 and 9999
        return String.format("CMSG%05d", randomNumber); // Formats the number as USERXXXXX
    }
}
