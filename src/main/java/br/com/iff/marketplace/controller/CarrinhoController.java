package br.com.iff.marketplace.controller;

import br.com.iff.marketplace.controller.dto.AddItemCarrinhoDTO;
import br.com.iff.marketplace.controller.dto.CarrinhoResponseDTO;
import br.com.iff.marketplace.model.CarrinhoDeCompras;
import br.com.iff.marketplace.service.CarrinhoService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import br.com.iff.marketplace.controller.dto.UpdateItemCarrinhoDTO;

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

    // Endpoint para buscar o carrinho de compras do usuário logado
    @GetMapping
    public ResponseEntity<CarrinhoResponseDTO> getMeuCarrinho() {
        CarrinhoDeCompras carrinho = service.getMeuCarrinho();

        return ResponseEntity.ok(new CarrinhoResponseDTO(carrinho));
    }

    // Endpoint para REMOVER um item do carrinho do usuário logado
    @DeleteMapping("/itens/{itemId}")
    public ResponseEntity<CarrinhoResponseDTO> removerItem(@PathVariable Long itemId) {
        CarrinhoDeCompras carrinhoAtualizado = service.removerItem(itemId);

        return ResponseEntity.ok(new CarrinhoResponseDTO(carrinhoAtualizado));
    }

    // Endpoint para ATUALIZAR a quantidade de um item no carrinho
    @PutMapping("/itens/{itemId}")
    public ResponseEntity<CarrinhoResponseDTO> atualizarQuantidadeItem(
            @PathVariable Long itemId,
            @RequestBody UpdateItemCarrinhoDTO dto) {

        CarrinhoDeCompras carrinhoAtualizado = service.atualizarQuantidadeItem(itemId, dto);

        return ResponseEntity.ok(new CarrinhoResponseDTO(carrinhoAtualizado));
    }
}