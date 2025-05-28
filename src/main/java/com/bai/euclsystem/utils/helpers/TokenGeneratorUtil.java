package com.bai.euclsystem.utils.helpers;

import com.bai.euclsystem.repositories.IMeterRepository;
import com.bai.euclsystem.repositories.IPurchasedTokenRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.security.SecureRandom;

@Component
@RequiredArgsConstructor
public class TokenGeneratorUtil {

    private static final SecureRandom random = new SecureRandom();
    private static final String ALPHANUMERIC = "ABCDEFGHIJKLMNOPQRSTUVWXYZ0123456789";
    private static final int METER_LENGTH = 6;
    private static final int TOKEN_LENGTH = 16;

    private final IPurchasedTokenRepository tokenRepository;
    private final IMeterRepository meterRepository;

    public String generateUniqueMeterNumber() {
        String meter;
        do {
            meter = randomString(METER_LENGTH);
        } while (meterRepository.findByMeterNumber(meter).isPresent());
        return meter;
    }

    public String generateUniqueToken() {
        String token;
        do {
            token = randomNumericString(TOKEN_LENGTH);
        } while (tokenRepository.findByToken(token).isPresent());
        return token;
    }

    private static String randomString(int length) {
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < length; i++) {
            sb.append(ALPHANUMERIC.charAt(random.nextInt(ALPHANUMERIC.length())));
        }
        return sb.toString();
    }

    private static String randomNumericString(int length) {
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < length; i++) {
            sb.append(random.nextInt(10));
        }
        return sb.toString();
    }
}
