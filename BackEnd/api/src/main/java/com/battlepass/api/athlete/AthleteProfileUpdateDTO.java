package com.battlepass.api.athlete;

import java.math.BigDecimal;
import java.util.Set;

public record AthleteProfileUpdateDTO(
        BigDecimal weightKg,
        BigDecimal heightCm,
        BigDecimal reachCm,
        String team,
        String coach,
        String grade,
        String category,
        String record,
        Set<Integer> modalityIds
) {}