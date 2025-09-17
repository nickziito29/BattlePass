package com.battlepass.api.user;

import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/users")
@CrossOrigin(origins = "http://localhost:5173")
public class UserController {

    @Autowired
    private UserService userService;

    // Endpoint público para registrar um novo usuário
    @PostMapping
    public ResponseEntity<User> registerUser(@Valid @RequestBody UserRegistrationDTO registrationData) {
        User newUser = userService.registerNewUser(registrationData);
        // Retorna 201 Created com o usuário criado
        return ResponseEntity.status(HttpStatus.CREATED).body(newUser);
    }

    // Endpoint protegido para que um usuário atualize seu próprio perfil
    @PutMapping("/profile")
    public ResponseEntity<User> updateUserProfile(
            Authentication authentication, // Objeto injetado pelo Spring Security
            @Valid @RequestBody ProfileUpdateDTO profileData)
    {
        // MELHORIA DE SEGURANÇA: Obtém o email do usuário logado diretamente do contexto de segurança.
        String userEmail = authentication.getName();

        User updatedUser = userService.updateUserProfile(userEmail, profileData);
        return ResponseEntity.ok(updatedUser);
    }
    @GetMapping("/me")
    public ResponseEntity<UserPublicDTO> getCurrentUser(Authentication authentication) {
        User user = (User) authentication.getPrincipal();

        UserPublicDTO dto = new UserPublicDTO(
                user.getId(),
                user.getFirstName(),
                user.getLastName(),
                user.getNickname(),
                user.getEmail(),
                user.getBirthDate(),
                user.getGender(),
                user.getProfilePictureUrl(),
                user.getBio(),
                user.getPronoun(),
                user.getCustomGender(),
                user.getHometown(),
                user.getCurrentCity(),
                user.getStatus(),
                user.getCreatedAt()
        );

        return ResponseEntity.ok(dto);
    }
}