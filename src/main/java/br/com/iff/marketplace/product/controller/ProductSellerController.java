package br.com.iff.marketplace.product.controller;

import br.com.iff.marketplace.product.dto.*;
import br.com.iff.marketplace.product.service.ProductService;
import br.com.iff.marketplace.user.User;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/seller/products")
@PreAuthorize("hasRole('SELLER')")
@RequiredArgsConstructor
public class ProductSellerController {

    private final ProductService productService;

    // Endpoint para criar/cadastrar um produto
    @PostMapping
    public ResponseEntity<ProductResponseDTO> createProduct(
            @RequestBody @Valid ProductRequestDTO productDTO,
            Authentication authentication) {

        User seller = (User) authentication.getPrincipal();

        ProductResponseDTO newProduct = productService.createProduct(productDTO, seller.getId());

        return ResponseEntity.status(HttpStatus.CREATED).body(newProduct);
    }

    // Endpoint para atualizar um produto
    @PutMapping("/{productId}")
    public ResponseEntity<ProductResponseDTO> updateProduct(
            @PathVariable Long productId,
            @RequestBody @Valid UpdateProductDTO updateDTO,
            Authentication authentication) {

        User seller = (User) authentication.getPrincipal();

        ProductResponseDTO updatedProduct = productService.updateProduct(productId, updateDTO, seller.getId());

        return ResponseEntity.ok(updatedProduct);
    }

    // Endpoint para deletar um produto
    @DeleteMapping("/{productId}")
    public ResponseEntity<Void> deleteProduct(@PathVariable Long productId, Authentication authentication) {

        User seller = (User) authentication.getPrincipal();

        productService.deleteProduct(productId, seller.getId());
        return ResponseEntity.noContent().build();
    }

    // Endpoint para adicionar uma variacao
    @PostMapping("/{productId}/variacoes")
    public ResponseEntity<ProductVariationResponseDTO> addVariation(
            @PathVariable Long productId,
            @RequestBody @Valid ProductVariationRequestDTO variationDTO,
            Authentication authentication) {

        User seller = (User) authentication.getPrincipal();

        ProductVariationResponseDTO newVariation = productService.addVariation(productId, variationDTO, seller.getId());

        return ResponseEntity.status(HttpStatus.CREATED).body(newVariation);
    }

    // Endpoint para atualizar uma variação de produto existente
    @PutMapping("/variacoes/{variationId}")
    public ResponseEntity<ProductVariationResponseDTO> updateVariation(
            @PathVariable Long variationId,
            @RequestBody @Valid ProductVariationRequestDTO variationDTO,
            Authentication authentication) {

        User seller = (User) authentication.getPrincipal();

        ProductVariationResponseDTO updatedVariation = productService.updateVariation(variationId, variationDTO, seller.getId());

        return ResponseEntity.ok(updatedVariation);
    }

    // Endpoint para um vendedor listar apenas os seus produtos
    @GetMapping
    public ResponseEntity<List<ProductResponseDTO>> listMyProducts(Authentication authentication) {

        User seller = (User) authentication.getPrincipal();

        List<ProductResponseDTO> productsList = productService.findProductBySeller(seller.getId());

        return ResponseEntity.ok(productsList);
    }
}
