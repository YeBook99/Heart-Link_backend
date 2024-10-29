package com.ss.heartlinkapi.login.service;

public final class CheckPassword {
    
    private static final String PASSWORD_REGEX = "^(?=.*[a-z])(?=.*\\d)(?=.*(_|[^\\w])).{7,16}$";

    private CheckPassword() {
    }

    public static boolean isPasswordValid(String password) {
        return password.matches(PASSWORD_REGEX);
    }
    
}