package com.zohar.educope.service.security;

import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

public class PasswordSecurity {

    public static String hashPasword(String password) {
        BCryptPasswordEncoder passwordEncoder = new BCryptPasswordEncoder();
        String hashedPassword = passwordEncoder.encode(password);
        return hashedPassword;
    }

}
