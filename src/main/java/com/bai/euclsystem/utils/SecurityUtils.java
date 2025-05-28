package com.bai.euclsystem.utils;

import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Component;

@Component
public class SecurityUtils {

    private static BCryptPasswordEncoder passwordEncoder = new BCryptPasswordEncoder();

    public static String  HashString(String  string){
        return passwordEncoder.encode(string);
    }

    public static boolean isTheSameHash(String newInput, String input){
        return passwordEncoder.matches(newInput,input);
    }
}
