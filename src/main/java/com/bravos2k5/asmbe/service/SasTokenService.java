package com.bravos2k5.asmbe.service;

import com.bravos2k5.asmbe.util.SasTokenGenerator;
import org.springframework.stereotype.Service;

@Service
public class SasTokenService {

    public String generateSasToken(String resource, String permissions, int durationInHours) {
        return SasTokenGenerator.generateSasToken(resource, permissions, durationInHours);
    }

}

