package com.elearning.validation;


import java.util.regex.Pattern;

public class ValidationUtil {

    private static final String EMAIL_PATTERN =
            "^[a-zA-Z0-9_+&*-]+(?:\\.[a-zA-Z0-9_+&*-]+)*@" +
                    "(?:[a-zA-Z0-9-]+\\.)+[a-zA-Z]{2,7}$";

    private static final Pattern emailPattern = Pattern.compile(EMAIL_PATTERN);

    public static boolean isValidEmail(String email) {
        return email != null && emailPattern.matcher(email).matches();
    }

    public static boolean isValidPassword(String password) {
        return password != null && password.length() >= 6;
    }

    public static boolean isValidUsername(String username) {
        return username != null && username.length() >= 3 && username.length() <= 20;
    }

    public static String validateSignupRequest(String username, String email, String password) {
        if (!isValidUsername(username)) {
            return "Username must be between 3 and 20 characters";
        }
        if (!isValidEmail(email)) {
            return "Invalid email format";
        }
        if (!isValidPassword(password)) {
            return "Password must be at least 6 characters long";
        }
        return null; // No validation errors
    }
}
