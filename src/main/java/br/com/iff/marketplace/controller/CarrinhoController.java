package br.com.iff.marketplace.controller;

import br.com.iff.marketplace.controller.dto.AddItemCarrinhoDTO;
import br.com.iff.marketplace.controller.dto.CarrinhoResponseDTO;
import br.com.iff.marketplace.model.CarrinhoDeCompras;
import br.com.iff.marketplace.service.CarrinhoService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/carrinho")
@RequiredArgsConstructor
public class CarrinhoController {

    private final CarrinhoService service;

    @PostMapping("/itens")
    public ResponseEntity<CarrinhoResponseDTO> adicionarItem(@RequestBody AddItemCarrinhoDTO dto) {
        CarrinhoDeCompras carrinhoAtualizado = service.adicionarItem(dto);
        return ResponseEntity.ok(new CarrinhoResponseDTO(carrinhoAtualizado));
    }

}