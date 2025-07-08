package br.com.iff.marketplace.controller;

import br.com.iff.marketplace.controller.dto.ProdutoRequestDTO;
import br.com.iff.marketplace.model.Produto;
import br.com.iff.marketplace.service.ProdutoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.util.List;
import br.com.iff.marketplace.controller.dto.ProdutoResponseDTO;

import lombok.extern.slf4j.Slf4j; // <-- IMPORT NOVO
import org.slf4j.Logger;         // <-- IMPORT NOVO
import org.slf4j.LoggerFactory; // <-- IMPORT NOVO

@RestController
@RequestMapping("/produtos")
@Slf4j
public class ProdutoController {

    @Autowired
    private ProdutoService service;

    @PostMapping
    public ResponseEntity<Produto> cadastrarProduto(@RequestBody ProdutoRequestDTO produtoDTO) {
        Produto novoProduto = service.salvarProduto(produtoDTO);
        return ResponseEntity.ok(novoProduto);
    }

    @GetMapping
    public ResponseEntity<List<ProdutoResponseDTO>> listarProdutos() {
        List<ProdutoResponseDTO> produtos = service.listarProdutos();
        return ResponseEntity.ok(produtos);
    }

    @PutMapping("/{id}")
    public ResponseEntity<ProdutoResponseDTO> atualizarProduto(@PathVariable Long id, @RequestBody ProdutoRequestDTO produtoDTO) {
        log.debug("CONTROLLER: Requisição PUT recebida para o produto ID: {}", id);
        Produto produtoAtualizado = service.atualizarProduto(id, produtoDTO);
        return ResponseEntity.ok(new ProdutoResponseDTO(produtoAtualizado));
    }

    // Endpoint para DELETAR um produto
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deletarProduto(@PathVariable Long id) {
        service.deletarProduto(id);
        return ResponseEntity.noContent().build();
    }
}