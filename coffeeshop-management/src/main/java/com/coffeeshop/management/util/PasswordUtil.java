package com.coffeeshop.management.util;

import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;

public class PasswordUtil {
    private static final PasswordEncoder encoder = new BCryptPasswordEncoder();

    public static String hashPassword(String rawPassword) {
        return encoder.encode(rawPassword);
    }

    public static boolean matches(String rawPassword, String encodedPassword) {
        return encoder.matches(rawPassword, encodedPassword);
    }

    public static String encodePassword(String rawPassword) {
        return hashPassword(rawPassword);
    }
}
