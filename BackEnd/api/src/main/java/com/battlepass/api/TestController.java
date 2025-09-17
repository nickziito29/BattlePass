package com.battlepass.api;

import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/test")
@CrossOrigin(origins = "http://localhost:5173")
public class TestController {

    @GetMapping("/hello")
    public ResponseEntity<String> sayHello(Authentication authentication) {
        // O Spring injeta o objeto 'Authentication' que nosso filtro criou.
        // Dele, podemos pegar o nome do usuário logado (que é o email).
        String userEmail = authentication.getName();
        return ResponseEntity.ok("Hello, authenticated user: " + userEmail);
    }
}