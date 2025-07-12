package br.com.iff.marketplace.controller;

import br.com.iff.marketplace.controller.dto.*;
import br.com.iff.marketplace.model.Produto;
import br.com.iff.marketplace.service.AvaliacaoService;
import br.com.iff.marketplace.service.ProdutoService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.util.List;
import java.math.BigDecimal;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import br.com.iff.marketplace.model.VariacaoProduto;

@RestController
@RequestMapping("/produtos")
@Slf4j
@RequiredArgsConstructor
public class ProdutoController {

    private final ProdutoService service;
    private final AvaliacaoService avaliacaoService;

    @PostMapping
    public ResponseEntity<Produto> cadastrarProduto(@RequestBody ProdutoRequestDTO produtoDTO) {
        Produto novoProduto = service.salvarProduto(produtoDTO);
        return ResponseEntity.ok(novoProduto);
    }

    @PostMapping("/{produtoId}/variacoes")
    public ResponseEntity<VariacaoResponseDTO> adicionarVariacao(
            @PathVariable Long produtoId,
            @RequestBody VariacaoRequestDTO variacaoDTO) {

        VariacaoProduto novaVariacao = service.adicionarVariacao(produtoId, variacaoDTO);

        return ResponseEntity.status(HttpStatus.CREATED).body(new VariacaoResponseDTO(novaVariacao));
    }

    @GetMapping
    public ResponseEntity<List<ProdutoResponseDTO>> listarProdutos(
            @RequestParam(value = "nome", required = false) String nome,
            @RequestParam(value = "categoriaId", required = false) Long categoriaId,
            @RequestParam(value = "precoMin", required = false) BigDecimal precoMin,
            @RequestParam(value = "precoMax", required = false) BigDecimal precoMax) {

        List<ProdutoResponseDTO> produtos = service.listarProdutos(nome, categoriaId, precoMin, precoMax);
        return ResponseEntity.ok(produtos);
    }

    @PutMapping("/{id}")
    public ResponseEntity<ProdutoResponseDTO> atualizarProduto(@PathVariable Long id, @RequestBody ProdutoRequestDTO produtoDTO) {
        Produto produtoAtualizado = service.atualizarProduto(id, produtoDTO);
        return ResponseEntity.ok(new ProdutoResponseDTO(produtoAtualizado));
    }

    // Endpoint para DELETAR um produto
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deletarProduto(@PathVariable Long id) {
        service.deletarProduto(id);
        return ResponseEntity.noContent().build();
    }

    // Endpoint para LISTAR todas as avaliações de um produto específico
    @GetMapping("/{produtoId}/avaliacoes")
    public ResponseEntity<List<AvaliacaoResponseDTO>> listarAvaliacoesDoProduto(@PathVariable Long produtoId) {
        List<AvaliacaoResponseDTO> avaliacoes = avaliacaoService.listarPorProduto(produtoId);
        return ResponseEntity.ok(avaliacoes);
    }

}