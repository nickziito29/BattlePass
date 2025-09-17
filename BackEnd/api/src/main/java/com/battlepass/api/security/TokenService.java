package com.battlepass.api.security;

import com.battlepass.api.user.User;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;
import org.springframework.stereotype.Service;

import javax.crypto.SecretKey;
import java.nio.charset.StandardCharsets;
import java.util.Date;

@Service
public class TokenService {

    // @Value("${jwt.secret}") // 1. COMENTAMOS A LEITURA DO ARQUIVO .properties
    // private String jwtSecret;

    // 2. ADICIONAMOS A CHAVE DIRETAMENTE NO CÓDIGO (UMA CHAVE FORTE E GARANTIDA)
    private final String HARDCODED_JWT_SECRET = "7f5d72a9a6b3c8e9f0d1b4a7c8e5f2a3b4c6d8e0f1a2b3c4d5e6f7a8b9c0d1e2";

    private static final long EXPIRATION_TIME = 864_000_000; // 10 dias em milissegundos

    public String generateToken(User user) {
        // 3. AGORA USAMOS A NOSSA CHAVE HARDCODED
        SecretKey secretKey = Keys.hmacShaKeyFor(HARDCODED_JWT_SECRET.getBytes(StandardCharsets.UTF_8));

        return Jwts.builder()
                .subject(user.getEmail())
                .issuedAt(new Date(System.currentTimeMillis()))
                .expiration(new Date(System.currentTimeMillis() + EXPIRATION_TIME))
                .signWith(secretKey)
                .compact();
    }

    public String getSubject(String token) {
        try {
            // 4. USAMOS A MESMA CHAVE HARDCODED PARA VALIDAR
            SecretKey secretKey = Keys.hmacShaKeyFor(HARDCODED_JWT_SECRET.getBytes(StandardCharsets.UTF_8));

            Claims claims = Jwts.parser()
                    .verifyWith(secretKey)
                    .build()
                    .parseSignedClaims(token)
                    .getPayload();

            return claims.getSubject();
        } catch (Exception e) {
            return null;
        }
    }
}