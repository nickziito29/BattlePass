package com.battlepass.api.modality;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ModalityService {

    @Autowired // Injeta a dependência do nosso repositório
    private ModalityRepository modalityRepository;

    /**
     * Busca todas as modalidades cadastradas no banco de dados.
     * @return Uma lista de entidades Modality.
     */
    public List<Modality> findAll() {
        return modalityRepository.findAll();
    }
}