package com.duongw.universalpetcare.utils;

import java.util.Random;

public class RandomUtils {


    private static final String[] EMAIL_DOMAINS = {"gmail.com", "yahoo.com", "outlook.com"};
    private static final Random RANDOM = new Random();


    public static String generateRandomEmail(String prefix) {
        String domain = EMAIL_DOMAINS[RANDOM.nextInt(EMAIL_DOMAINS.length)];
        int randomNumber = RANDOM.nextInt(10000); // Số ngẫu nhiên để tạo email duy nhất
        return prefix + randomNumber + "@" + domain;
    }

    // Hàm tạo số điện thoại ngẫu nhiên
    public static String generateRandomPhoneNumber() {
        StringBuilder phoneNumber = new StringBuilder("0");
        for (int i = 0; i < 9; i++) {
            phoneNumber.append(RANDOM.nextInt(10)); // Mỗi chữ số trong số điện thoại là số từ 0 đến 9
        }
        return phoneNumber.toString();
    }
}


