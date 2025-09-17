package com.battlepass.api.athlete;

import com.battlepass.api.modality.Modality;
import com.battlepass.api.user.User;
import jakarta.persistence.*;
import lombok.*;
import java.math.BigDecimal; // <-- GARANTA QUE ESTE IMPORT ESTEJA AQUI
import java.util.HashSet;
import java.util.Set;
import java.util.UUID;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "athlete_profiles")
public class AthleteProfile {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;

    // --- GARANTA QUE OS TIPOS ESTEJAM COMO BigDecimal ---
    private BigDecimal weightKg;
    private BigDecimal heightCm;
    private BigDecimal reachCm;

    private String team;
    private String coach;
    private String grade;
    private String category;
    private String record;

    @OneToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "user_id", nullable = false)
    private User user;

    @ManyToMany(fetch = FetchType.LAZY)
    @JoinTable(
            name = "athlete_modalities",
            joinColumns = @JoinColumn(name = "athlete_profile_id"),
            inverseJoinColumns = @JoinColumn(name = "modality_id")
    )
    private Set<Modality> modalities = new HashSet<>();
}