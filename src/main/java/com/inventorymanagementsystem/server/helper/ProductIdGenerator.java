package com.inventorymanagementsystem.server.helper;

import java.util.Random;

public class ProductIdGenerator {
    private static final Random RANDOM = new Random();

    public static String generateProductId() {
        int randomNumber = RANDOM.nextInt(10000); // Generates a number between 0 and 9999
        return String.format("PR%04d", randomNumber); // Formats the number as PRXXXX
    }
}

