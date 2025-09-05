package br.com.iff.marketplace.product.controller;

import br.com.iff.marketplace.product.dto.*;
import br.com.iff.marketplace.product.service.ProductService;
import br.com.iff.marketplace.user.User;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/seller/products")
@PreAuthorize("hasRole('SELLER')")
@RequiredArgsConstructor
public class ProductSellerController {

    private final ProductService productService;

    @GetMapping
    public ResponseEntity<Page<ProductResponseDTO>> listMyProducts(
            @AuthenticationPrincipal User seller,
            Pageable pageable) {

        Page<ProductResponseDTO> productsList = productService.findProductBySeller(seller.getId(), pageable);
        return ResponseEntity.ok(productsList);
    }

    @PostMapping
    public ResponseEntity<ProductResponseDTO> createProduct(
            @RequestBody @Valid ProductRequestDTO productDTO,
            @AuthenticationPrincipal User seller) {

        ProductResponseDTO newProduct = productService.createProduct(productDTO, seller.getId());
        return ResponseEntity.status(HttpStatus.CREATED).body(newProduct);
    }

    @PutMapping("/{productId}")
    public ResponseEntity<ProductResponseDTO> updateProduct(
            @PathVariable Long productId,
            @RequestBody @Valid UpdateProductDTO updateDTO,
            @AuthenticationPrincipal User seller) {

        ProductResponseDTO updatedProduct = productService.updateProduct(productId, updateDTO, seller.getId());
        return ResponseEntity.ok(updatedProduct);
    }

    @DeleteMapping("/{productId}")
    public ResponseEntity<Void> deleteProduct(
            @PathVariable Long productId,
            @AuthenticationPrincipal User seller) {

        productService.deleteProduct(productId, seller.getId());
        return ResponseEntity.noContent().build();
    }

    @PostMapping("/{productId}/variation")
    public ResponseEntity<ProductVariationResponseDTO> addVariation(
            @PathVariable Long productId,
            @RequestBody @Valid ProductVariationRequestDTO variationDTO,
            @AuthenticationPrincipal User seller) {

        ProductVariationResponseDTO newVariation = productService.addVariation(productId, variationDTO, seller.getId());
        return ResponseEntity.status(HttpStatus.CREATED).body(newVariation);
    }

    @PutMapping("{productId}/variation/{variationId}")
    public ResponseEntity<ProductVariationResponseDTO> updateVariation(
            @PathVariable Long productId,
            @PathVariable Long variationId,
            @RequestBody @Valid ProductVariationRequestDTO variationDTO,
            @AuthenticationPrincipal User seller) {

        ProductVariationResponseDTO updatedVariation = productService.updateVariation(productId, variationId, variationDTO, seller.getId());
        return ResponseEntity.ok(updatedVariation);
    }

    @DeleteMapping("/{productId}/variation/{variationId}")
    public ResponseEntity<Void> deleteVariation(
            @PathVariable Long productId,
            @PathVariable Long variationId,
            @AuthenticationPrincipal User seller) {

        productService.deleteVariation(productId, variationId, seller.getId());
        return ResponseEntity.noContent().build();
    }

}
