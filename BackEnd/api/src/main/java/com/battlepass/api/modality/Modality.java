package com.battlepass.api.modality;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter; // Alterado de @Data
import lombok.NoArgsConstructor;
import lombok.Setter; // Alterado de @Data

@Getter // MELHORIA: Usar anotações específicas em vez de @Data
@Setter // MELHORIA: Usar anotações específicas em vez de @Data
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "modalities")
public class Modality {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @Column(unique = true, nullable = false)
    private String name;

    @Column(length = 500)
    private String description;
}