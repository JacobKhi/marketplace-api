package br.com.iff.marketplace.product.controller;

import br.com.iff.marketplace.review.dto.ReviewResponseDTO;
import br.com.iff.marketplace.product.dto.ProductResponseDTO;
import br.com.iff.marketplace.product.service.ProductService;
import br.com.iff.marketplace.review.service.ReviewService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.math.BigDecimal;
import java.util.List;

@RestController
@RequestMapping("/api/v1/products")
@RequiredArgsConstructor
public class ProductPublicController {
    
    private final ProductService productService;
    private final ReviewService reviewService;
    
    // Endpoint para listar todos os produtos
    @GetMapping
    public ResponseEntity<List<ProductResponseDTO>> listAllProducts(
            @RequestParam(value = "name", required = false) String name,
            @RequestParam(value = "categoryId", required = false) Long categoryId,
            @RequestParam(value = "minPrice", required = false) BigDecimal minPrice,
            @RequestParam(value = "maxPrice", required = false) BigDecimal maxPrice) {

        List<ProductResponseDTO> products = productService.searchProducts(name, categoryId, minPrice, maxPrice);

        return ResponseEntity.ok(products);
    }

    // Endpoint para buscar um produto por id
    @GetMapping("/{productId}")
    public ResponseEntity<ProductResponseDTO> findProductById(@PathVariable Long productId) {

        ProductResponseDTO product = productService.findProductById(productId);

        return ResponseEntity.ok(product);
    }

    // Mais tarde criar uma classe ReviewController que é responsabilidade das avalaiacões
    // Endpoint para lsitar todas as avaliações de um produto específico
    @GetMapping("/{productId}/reviews")
    public ResponseEntity<List<ReviewResponseDTO>> listProductReviews(@PathVariable Long productId) {

        List<ReviewResponseDTO> reviews = reviewService.listarPorProduto(productId);

        return ResponseEntity.ok(reviews);
    }
    
}
