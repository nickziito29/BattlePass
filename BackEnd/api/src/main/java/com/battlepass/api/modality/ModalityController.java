package com.battlepass.api.modality;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api/modalities") // Define a URL base para este controller
@CrossOrigin(origins = "http://localhost:5173") // Permite acesso do nosso frontend React
public class ModalityController {

    @Autowired // Injeta a dependência do nosso serviço
    private ModalityService modalityService;

    @GetMapping // Mapeia requisições HTTP GET para este método
    public ResponseEntity<List<Modality>> getAllModalities() {
        List<Modality> modalities = modalityService.findAll();
        return ResponseEntity.ok(modalities); // Retorna 200 OK e a lista de modalidades no corpo da resposta
    }
}