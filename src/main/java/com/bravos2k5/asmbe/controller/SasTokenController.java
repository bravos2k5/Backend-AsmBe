package com.bravos2k5.asmbe.controller;

import com.bravos2k5.asmbe.service.SasTokenService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class SasTokenController {

    private final SasTokenService sasTokenService;

    @Autowired
    public SasTokenController(SasTokenService sasTokenService) {
        this.sasTokenService = sasTokenService;
    }

    @GetMapping("/api/private/getsaskey")
    public ResponseEntity<String> generateSasToken() {
        return new ResponseEntity<>(sasTokenService.generateSasToken("image","rw",1), HttpStatus.OK);
    }

}
