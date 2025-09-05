package br.com.iff.marketplace.product.controller;

import br.com.iff.marketplace.product.service.ProductSearchService;
import br.com.iff.marketplace.review.dto.ReviewResponseDTO;
import br.com.iff.marketplace.product.dto.ProductResponseDTO;
import br.com.iff.marketplace.product.service.ProductService;
import br.com.iff.marketplace.review.service.ReviewService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.math.BigDecimal;
import java.util.List;

@RestController
@RequestMapping("/api/v1/products")
@RequiredArgsConstructor
public class ProductPublicController {
    
    private final ProductSearchService productSearchService;

    @GetMapping
    public ResponseEntity<Page<ProductResponseDTO>> listAllProducts(
            @RequestParam(value = "name", required = false) String name,
            @RequestParam(value = "categoryId", required = false) Long categoryId,
            @RequestParam(value = "minPrice", required = false) BigDecimal minPrice,
            @RequestParam(value = "maxPrice", required = false) BigDecimal maxPrice,
            Pageable pageable) {

        Page<ProductResponseDTO> productsPage = productSearchService.searchProducts(name, categoryId, minPrice, maxPrice, pageable);
        return ResponseEntity.ok(productsPage);
    }

    @GetMapping("/{productId}")
    public ResponseEntity<ProductResponseDTO> findProductById(@PathVariable Long productId) {

        ProductResponseDTO product = productSearchService.findProductById(productId);
        return ResponseEntity.ok(product);
    }

}
