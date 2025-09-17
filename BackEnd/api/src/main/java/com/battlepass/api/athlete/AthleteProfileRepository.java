package com.battlepass.api.athlete;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;
import java.util.UUID;

@Repository
public interface AthleteProfileRepository extends JpaRepository<AthleteProfile, UUID> {
    // JpaRepository<TipoDaEntidade, TipoDoIdDaEntidade>

    // Um método útil para o futuro: encontrar um perfil de atleta pelo ID do usuário
    Optional<AthleteProfile> findByUser_Id(String userId);
}