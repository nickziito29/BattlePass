package com.battlepass.api.security;

import com.battlepass.api.user.User;
import com.battlepass.api.user.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/auth")
@CrossOrigin(origins = "http://localhost:5173")
public class AuthenticationController {

    @Autowired
    private UserService userService;

    @Autowired
    private AuthenticationManager authenticationManager;

    @Autowired
    private TokenService tokenService;

    @PostMapping("/login")
    public ResponseEntity<?> login(@RequestBody LoginRequestDTO loginRequest) {
        // LINHA DE DEBUG:
        System.out.println(">>> DENTRO DO MÉTODO DE LOGIN <<<");

        var usernamePassword = new UsernamePasswordAuthenticationToken(loginRequest.email(), loginRequest.password());
        var auth = this.authenticationManager.authenticate(usernamePassword);

        var user = (User) auth.getPrincipal();
        String token = tokenService.generateToken(user);

        return ResponseEntity.ok(new LoginResponseDTO(token));
    }

    @GetMapping("/verify")
    public ResponseEntity<String> verifyAccount(@RequestParam("token") String token) {
        try {
            userService.verifyUser(token);
            // Idealmente, redirecionar para uma página de sucesso no front-end
            // return ResponseEntity.status(HttpStatus.FOUND).location(URI.create("http://localhost:5173/verification-success")).build();
            return ResponseEntity.ok("Conta verificada com sucesso! Você já pode fazer o login.");
        } catch (IllegalStateException e) {
            // Idealmente, redirecionar para uma página de erro no front-end
            // return ResponseEntity.status(HttpStatus.FOUND).location(URI.create("http://localhost:5173/verification-error?message=" + e.getMessage())).build();
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }
}