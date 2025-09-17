package com.battlepass.api;

import com.battlepass.api.modality.Modality;
import com.battlepass.api.modality.ModalityRepository;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import java.util.List;

@SpringBootApplication
public class ApiApplication { // <-- Classe começa aqui

    // --- MÉTODO 1 ---
    public static void main(String[] args) {
        SpringApplication.run(ApiApplication.class, args);
    } // <-- MÉTODO 1 (main) TERMINA AQUI

    // --- MÉTODO 2 ---
    @Bean // Este método agora está no nível da classe, o que é o correto
    CommandLineRunner initDatabase(ModalityRepository repository) {
        return args -> {
            if (repository.count() == 0) {
                System.out.println(">>> Preloading database with 15 modalities...");

                List<Modality> modalities = List.of(
                        new Modality(null, "Jiu-Jitsu Brasileiro", "Arte marcial focada em luta de chão e finalizações."),
                        new Modality(null, "Boxe", "Esporte de combate focado no uso dos punhos."),
                        new Modality(null, "Muay Thai", "Arte marcial tailandesa conhecida como a arte dos oito membros."),
                        new Modality(null, "MMA (Mixed Martial Arts)", "Combinação de várias artes marciais em um único esporte."),
                        new Modality(null, "Wrestling (Luta Olímpica)", "Estilo de luta agarrada com foco em quedas e controle no solo."),
                        new Modality(null, "Judô", "Arte marcial japonesa moderna, focada em arremessos e imobilizações."),
                        new Modality(null, "Karatê", "Arte marcial de Okinawa com foco em golpes de mão e pés."),
                        new Modality(null, "Taekwondo", "Arte marcial coreana conhecida por seus chutes altos e rápidos."),
                        new Modality(null, "Kickboxing", "Combinação de socos do boxe com chutes de artes marciais."),
                        new Modality(null, "Sambo", "Arte marcial e esporte de combate russo com origens no judô e wrestling."),
                        new Modality(null, "Capoeira", "Expressão cultural brasileira que mistura arte marcial, esporte e música."),
                        new Modality(null, "Krav Maga", "Sistema de combate e autodefesa desenvolvido em Israel."),
                        new Modality(null, "Sumô", "Luta tradicional japonesa onde o objetivo é forçar o oponente para fora do ringue."),
                        new Modality(null, "Aikido", "Arte marcial japonesa que utiliza o movimento e a energia do oponente."),
                        new Modality(null, "Savate (Boxe Francês)", "Arte marcial francesa que utiliza pés e mãos como armas.")
                );

                repository.saveAll(modalities);
                System.out.println(">>> 15 Modalities loaded!");
            }
        };
    } // <-- MÉTODO 2 (initDatabase) TERMINA AQUI

} // <-- Classe termina aqui