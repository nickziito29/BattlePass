package com.battlepass.api.athlete;

import java.math.BigDecimal;
import java.util.Set;
import java.util.UUID;

public record AthleteProfileResponseDTO(
        UUID id,
        BigDecimal weightKg,
        BigDecimal heightCm,
        BigDecimal reachCm,
        String team,
        String coach,
        String grade,
        String category,
        String record,
        Set<ModalityDTO> modalities
) {
    public record ModalityDTO(Integer id, String name) {
    }
}