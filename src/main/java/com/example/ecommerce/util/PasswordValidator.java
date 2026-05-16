package com.example.ecommerce.util;

import java.util.regex.Pattern;

public class PasswordValidator {

    private static final String PASSWORD_PATTERN = 
        "^(?=.*[A-Z])" +           // at least one uppercase letter
        "(?=.*[a-z])" +            // at least one lowercase letter
        "(?=.*\\d)" +              // at least one digit
        "(?=.*[!@#$%^&*])" +       // at least one special symbol
        ".{8,}$";                  // minimum 8 characters

    private static final Pattern PATTERN = Pattern.compile(PASSWORD_PATTERN);

    public static boolean isValid(String password) {
        if (password == null || password.isEmpty()) {
            return false;
        }
        return PATTERN.matcher(password).matches();
    }

    public static String getValidationError() {
        return "Password must contain min 8 characters, uppercase, lowercase, number, and special symbol";
    }
}
