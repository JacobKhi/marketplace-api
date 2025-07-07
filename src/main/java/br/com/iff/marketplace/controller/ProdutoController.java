package br.com.iff.marketplace.controller;

import br.com.iff.marketplace.controller.dto.ProdutoRequestDTO;
import br.com.iff.marketplace.model.Produto;
import br.com.iff.marketplace.service.ProdutoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.util.List;
import br.com.iff.marketplace.controller.dto.ProdutoResponseDTO;

@RestController
@RequestMapping("/produtos")
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
}