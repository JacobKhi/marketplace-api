package br.com.iff.marketplace.controller;

import br.com.iff.marketplace.controller.dto.AvaliacaoRequestDTO;
import br.com.iff.marketplace.controller.dto.AvaliacaoResponseDTO;
import br.com.iff.marketplace.model.Avaliacao;
import br.com.iff.marketplace.service.AvaliacaoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/avaliacoes")
public class AvaliacaoController {

    @Autowired
    private AvaliacaoService service;

    @PostMapping
    public ResponseEntity<AvaliacaoResponseDTO> criarAvaliacao(@RequestBody AvaliacaoRequestDTO dto) {
        Avaliacao novaAvaliacao = service.criarAvaliacao(dto);
        return ResponseEntity.ok(new AvaliacaoResponseDTO(novaAvaliacao));
    }
}