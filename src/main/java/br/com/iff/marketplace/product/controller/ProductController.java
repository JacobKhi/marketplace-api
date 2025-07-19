package br.com.iff.marketplace.product.controller;

import br.com.iff.marketplace.controller.dto.*;
import br.com.iff.marketplace.product.Product;
import br.com.iff.marketplace.product.dto.ProductVariationRequestDTO;
import br.com.iff.marketplace.product.service.ProductService;
import br.com.iff.marketplace.product.dto.ProductRequestDTO;
import br.com.iff.marketplace.product.dto.ProductResponseDTO;
import br.com.iff.marketplace.service.AvaliacaoService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import java.util.List;
import java.math.BigDecimal;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import br.com.iff.marketplace.product.ProductVariation;
import org.springframework.web.bind.annotation.PathVariable;

@RestController
@RequestMapping("/produtos")
@Slf4j
@RequiredArgsConstructor
public class ProductController {

    private final ProductService service;
    private final AvaliacaoService avaliacaoService;

    @PostMapping
    public ResponseEntity<Product> cadastrarProduto(@RequestBody ProductRequestDTO produtoDTO) {
        Product novoProduto = service.createProduct(produtoDTO);
        return ResponseEntity.ok(novoProduto);
    }

    @PostMapping("/{produtoId}/variacoes")
    public ResponseEntity<VariacaoResponseDTO> adicionarVariacao(
            @PathVariable Long produtoId,
            @RequestBody ProductVariationRequestDTO variacaoDTO) {

        ProductVariation novaVariacao = service.addVariation(produtoId, variacaoDTO);

        return ResponseEntity.status(HttpStatus.CREATED).body(new VariacaoResponseDTO(novaVariacao));
    }

    @GetMapping
    public ResponseEntity<List<ProductResponseDTO>> listarProdutos(
            @RequestParam(value = "nome", required = false) String nome,
            @RequestParam(value = "categoriaId", required = false) Long categoriaId,
            @RequestParam(value = "precoMin", required = false) BigDecimal precoMin,
            @RequestParam(value = "precoMax", required = false) BigDecimal precoMax) {

        List<ProductResponseDTO> produtos = service.searchProducts(nome, categoriaId, precoMin, precoMax);
        return ResponseEntity.ok(produtos);
    }

    @PutMapping("/{id}")
    public ResponseEntity<ProductResponseDTO> atualizarProduto(@PathVariable Long id, @RequestBody ProductRequestDTO produtoDTO) {
        Product produtoAtualizado = service.updateProduct(id, produtoDTO);
        return ResponseEntity.ok(new ProductResponseDTO(produtoAtualizado));
    }

    // Endpoint para DELETAR um produto
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deletarProduto(@PathVariable Long id) {
        service.deleteProduct(id);
        return ResponseEntity.noContent().build();
    }

    // Endpoint para LISTAR todas as avaliações de um produto específico
    @GetMapping("/{produtoId}/avaliacoes")
    public ResponseEntity<List<AvaliacaoResponseDTO>> listarAvaliacoesDoProduto(@PathVariable Long produtoId) {
        List<AvaliacaoResponseDTO> avaliacoes = avaliacaoService.listarPorProduto(produtoId);
        return ResponseEntity.ok(avaliacoes);
    }

    // ENDPOINT para buscar um produto por ID
    @GetMapping("/{id}")
    public ResponseEntity<ProductResponseDTO> buscarProdutoPorId(@PathVariable Long id) {
        Product produto = service.findProductById(id);
        return ResponseEntity.ok(new ProductResponseDTO(produto));
    }

    // Endpoint para ATUALIZAR uma variação de produto existente
    @PutMapping("/variacoes/{variacaoId}")
    public ResponseEntity<VariacaoResponseDTO> atualizarVariacao(
            @PathVariable Long variacaoId,
            @RequestBody ProductVariationRequestDTO variacaoDTO) {

        ProductVariation variacaoAtualizada = service.updateVariation(variacaoId, variacaoDTO);

        return ResponseEntity.ok(new VariacaoResponseDTO(variacaoAtualizada));
    }

    // Endpoint para um VENDEDOR listar apenas os SEUS produtos
    @GetMapping("/vendedor/meus-produtos")
    @PreAuthorize("hasRole('VENDEDOR')")
    public ResponseEntity<List<ProductResponseDTO>> listarMeusProdutos() {
        List<ProductResponseDTO> produtos = service.findProductBySeller();
        return ResponseEntity.ok(produtos);
    }
}