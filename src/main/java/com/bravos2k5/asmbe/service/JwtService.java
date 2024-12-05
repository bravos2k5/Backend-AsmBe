package com.bravos2k5.asmbe.service;

import com.bravos2k5.asmbe.model.RefreshToken;
import com.bravos2k5.asmbe.util.KeyUtils;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.JwtException;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.InvalidKeyException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.security.PrivateKey;
import java.security.PublicKey;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;
import java.util.function.Function;

@Service
public class JwtService {

    private final PrivateKey privateKey;
    private final PublicKey publicKey;
    private final RefreshTokenService refreshTokenService;

    @Autowired
    public JwtService(RefreshTokenService refreshTokenService) {
        this.privateKey = KeyUtils.loadPrivateKey(System.getenv("PRIVATE_KEY_PATH"),"16122005");
        this.publicKey = KeyUtils.loadPublicKey(System.getenv("PUBLIC_KEY_PATH"));
        this.refreshTokenService = refreshTokenService;
    }

    public String generateToken(Map<String,Object> claims, long expSeconds) {
        try {
            return Jwts.builder()
                    .claims()
                    .add(claims)
                    .issuedAt(new Date())
                    .expiration(new Date(System.currentTimeMillis() + expSeconds * 1000))
                    .and()
                    .signWith(privateKey,Jwts.SIG.RS256)
                    .compact();
        } catch (InvalidKeyException e) {
            System.err.println(e.getMessage());
            return null;
        }
    }

    public String generateToken(UUID refreshTokenId, long expSeconds) {
        RefreshToken refreshToken = refreshTokenService.getRefreshTokenIfValid(refreshTokenId);
        if(refreshToken == null)  {
            return null;
        }
        Map<String,Object> claims = new HashMap<>();
        claims.put("sub",refreshToken.getUser().getUsername());
        return generateToken(claims,expSeconds);
    }

    public Claims extractAllClaims(String token) {
        try {
            return Jwts.parser()
                    .verifyWith(publicKey)
                    .build()
                    .parseSignedClaims(token)
                    .getPayload();
        } catch (JwtException e) {
            return null;
        } catch (IllegalArgumentException e) {
            throw new RuntimeException(e);
        }
    }

    public <T> T extractClaim(String token, Function<Claims,T> claimsResolver) {
        Claims claims = this.extractAllClaims(token);
        if(claims == null) {
            return null;
        }
        return claimsResolver.apply(claims);
    }

    public boolean isExpiredToken(String token) {
        Date exp = extractClaim(token,Claims::getExpiration);
        if(exp == null) {
            return true;
        }
        return exp.before(new Date());
    }

    public boolean validateToken(String token, String username) {
        Claims claims = this.extractAllClaims(token);
        return claims != null && claims.getExpiration().after(new Date())
                && claims.getSubject().equals(username)
                && claims.getIssuedAt().before(new Date());
    }

}
