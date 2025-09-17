package com.battlepass.api.user;

public record ProfileUpdateDTO(
        String bio,
        String pronoun,
        String hometown,
        String currentCity
) {
}