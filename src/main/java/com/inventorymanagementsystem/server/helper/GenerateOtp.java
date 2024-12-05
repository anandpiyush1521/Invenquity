package com.inventorymanagementsystem.server.helper;

import java.util.Random;

public class GenerateOtp {
    public static String generateOtp() {
        Random random = new Random();
        int otpValue = 100000 + random.nextInt(900000);
        return String.valueOf(otpValue);
    }
}
