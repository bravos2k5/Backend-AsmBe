package com.bravos2k5.asmbe.util;

public class RegexUtils {

    public static final int HARD = 3;
    public static final int MEDIUM = 2;
    public static final int EASY = 1;
    public static final int CANT_USE = 0;

    public static int checkPassword(String password) {
        if(password == null || password.isBlank()) {
            return CANT_USE;
        }
        String regexEasy = ".{6,}";
        String regexMedium = "^(?=.*[a-z])(?=.*[A-Z])(?=.*\\d).{8,}$";
        String regexHard = "^(?=.*[a-z])(?=.*[A-Z])(?=.*\\d)(?=.*[@$!%*?&]).{10,}$";
        if (password.matches(regexHard)) {
            return HARD;
        }
        if (password.matches(regexMedium)) {
            return MEDIUM;
        }
        if (password.matches(regexEasy)) {
            return EASY;
        }
        return CANT_USE;
    }

    public static boolean isPhone(String phone) {
        String regex = "^(?:\\+\\d{1,14}|\\d{1,14})$";
        return phone.matches(regex);
    }

    public static boolean isEmail(String email) {
        String regex = "^[a-zA-Z0-9_+&*-]+(?:\\.[a-zA-Z0-9_+&*-]+)*@(?:[a-zA-Z0-9-]+\\.)+[a-zA-Z]{2,7}$";
        return email != null && !email.isBlank() && email.matches(regex);
    }

    public static boolean isUsername(String username) {
        return username != null && !username.isBlank() && username.matches("^[a-zA-Z0-9]{5,}$\n");
    }

    public static String maskEmail(String email) {
        String[] emailParts = email.split("@");
        StringBuilder maskEmail = new StringBuilder();
        int hiddenUsername = (int)Math.ceil((double) emailParts[0].length() / 2);
        int hiddenDomainName = (int) Math.floor((double) emailParts[1].length() / 2);
        maskEmail.append(emailParts[0], 0, hiddenUsername);
        maskEmail.append("*".repeat(emailParts[0].length() - hiddenUsername));
        maskEmail.append("@").append("*".repeat(hiddenDomainName));
        maskEmail.append(emailParts[1], hiddenDomainName, emailParts[1].length());
        return maskEmail.toString().trim();
    }

}
