package com.battlepass.api.user;

import java.time.LocalDate;
import java.time.LocalDateTime;

public record UserPublicDTO(
        java.util.UUID id,
        String firstName,
        String lastName,
        String nickname,
        String email,
        LocalDate birthDate,
        Gender gender,
        String profilePictureUrl,
        String bio,
        String pronoun,
        String customGender,
        String hometown,
        String currentCity,
        AccountStatus status,
        LocalDateTime createdAt
) {}