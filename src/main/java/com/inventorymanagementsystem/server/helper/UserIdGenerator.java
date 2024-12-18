package com.inventorymanagementsystem.server.helper;

import java.util.Random;

public class UserIdGenerator {
    private static final Random RANDOM = new Random();

    public static String generateProductId() {
        int randomNumber = RANDOM.nextInt(100000); // Generates a number between 0 and 9999
        return String.format("USER%05d", randomNumber); // Formats the number as USERXXXXX
    }
}