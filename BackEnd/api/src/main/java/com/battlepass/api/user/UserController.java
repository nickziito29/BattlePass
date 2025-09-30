package com.battlepass.api.user;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor; // Usando Lombok para injeção
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/users")
@RequiredArgsConstructor // Injeta dependências via construtor
public class UserController {

    private final UserService userService; // Injetado via @RequiredArgsConstructor

    // Endpoint público para registrar um novo usuário
    @PostMapping
    public ResponseEntity<UserPublicDTO> registerUser(@Valid @RequestBody UserRegistrationDTO registrationData) {
        User newUser = userService.registerNewUser(registrationData);
        // Retorna 201 Created com o DTO público do usuário, NUNCA a entidade completa
        return ResponseEntity.status(HttpStatus.CREATED).body(mapEntityToDto(newUser));
    }

    // Endpoint protegido para que um usuário atualize seu próprio perfil
    @PutMapping("/profile")
    public ResponseEntity<UserPublicDTO> updateUserProfile(
            @AuthenticationPrincipal User user, // Injeta o usuário logado diretamente
            @Valid @RequestBody ProfileUpdateDTO profileData)
    {
        User updatedUser = userService.updateUserProfile(user.getEmail(), profileData);
        return ResponseEntity.ok(mapEntityToDto(updatedUser));
    }

    // Endpoint para buscar os dados do usuário logado
    @GetMapping("/me")
    public ResponseEntity<UserPublicDTO> getCurrentUser(@AuthenticationPrincipal User user) {
        return ResponseEntity.ok(mapEntityToDto(user));
    }

    // Endpoint para marcar o onboarding como completo
    @PutMapping("/me/complete-onboarding")
    public ResponseEntity<Void> completeOnboarding(@AuthenticationPrincipal User user) {
        userService.completeOnboarding(user);
        return ResponseEntity.ok().build();
    }

    // Método helper privado para mapear a Entidade User para o DTO público
    private UserPublicDTO mapEntityToDto(User user) {
        return new UserPublicDTO(
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
                user.getCreatedAt(),
                user.getIsNewUser(),
                user.getStatus() // <-- CAMPO ADICIONADO AQUI
        );
    }
}