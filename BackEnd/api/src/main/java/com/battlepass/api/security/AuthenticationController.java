package com.battlepass.api.security;

import com.battlepass.api.user.User;
import com.battlepass.api.user.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.web.bind.annotation.*;

import java.net.URI;

@RestController
@RequestMapping("/api/auth")
// A anotação @CrossOrigin é opcional aqui se você já configurou o CORS globalmente no WebConfig
public class AuthenticationController {

    @Autowired
    private UserService userService;

    @Autowired
    private AuthenticationManager authenticationManager;

    @Autowired
    private TokenService tokenService;

    @PostMapping("/login")
    public ResponseEntity<LoginResponseDTO> login(@RequestBody LoginRequestDTO loginRequest) {
        var usernamePassword = new UsernamePasswordAuthenticationToken(loginRequest.email(), loginRequest.password());
        var auth = this.authenticationManager.authenticate(usernamePassword);

        var user = (User) auth.getPrincipal();
        String token = tokenService.generateToken(user);

        return ResponseEntity.ok(new LoginResponseDTO(token));
    }

    /**
     * Endpoint que recebe o token de verificação enviado por e-mail.
     * Se o token for válido, ativa o usuário e redireciona para uma página de sucesso no front-end.
     */
    @GetMapping("/verify")
    public ResponseEntity<Void> verifyAccount(@RequestParam("token") String token) {
        try {
            userService.verifyUser(token);
            // SUCESSO: Retorna um status 302 (Found) que instrui o navegador a redirecionar
            return ResponseEntity.status(HttpStatus.FOUND)
                    .location(URI.create("http://localhost:5173/verification-success"))
                    .build();
        } catch (IllegalStateException e) {
            // FALHA: Redireciona para uma futura página de erro no front-end (opcional)
            // Por enquanto, apenas retorna um erro 400.
            // No futuro, você poderia usar:
            // return ResponseEntity.status(HttpStatus.FOUND).location(URI.create("http://localhost:5173/verification-error")).build();
            return ResponseEntity.badRequest().build();
        }
    }
}