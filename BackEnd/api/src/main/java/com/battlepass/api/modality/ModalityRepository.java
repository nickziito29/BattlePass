package com.battlepass.api.modality;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository // Boa prática para indicar que esta é uma interface de acesso a dados
public interface ModalityRepository extends JpaRepository<Modality, Integer> {
    // JpaRepository<TipoDaEntidade, TipoDoIdDaEntidade>

    // Podemos adicionar métodos de busca customizados aqui.
    // O Spring Data JPA cria a query automaticamente a partir do nome do método.
    Optional<Modality> findByName(String name);
}