package com.battlepass.api.user;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

import java.time.LocalDate;

// Usamos um 'record' para um DTO imutável e conciso.
// As anotações de validação garantem que os dados cheguem corretamente.
public record UserRegistrationDTO(
        @NotBlank String firstName,
        @NotBlank String lastName,
        String nickname, // Opcional, então não precisa de @NotBlank
        @NotBlank @Email String email,
        @NotBlank @Size(min = 8, message = "Password must be at least 8 characters long") String password,
        @NotNull LocalDate birthDate,
        @NotNull Gender gender,
        String pronoun, // Opcional
        String customGender // Opcional
) {
}