package br.com.iff.marketplace.cart.controller;

import br.com.iff.marketplace.cart.dto.CartResponseDTO;
import br.com.iff.marketplace.cart.dto.UpdateItemQuantityDTO;
import br.com.iff.marketplace.cart.service.CartService;
import br.com.iff.marketplace.cart.dto.AddItemToCartDTO;
import br.com.iff.marketplace.user.User;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/cart")
@PreAuthorize("hasRole('CUSTOMER')")
@RequiredArgsConstructor
public class CartController {

    private final CartService cartService;

    @GetMapping
    public ResponseEntity<CartResponseDTO> getMyCart(@AuthenticationPrincipal User customer) {

        CartResponseDTO cart = cartService.getCartForUser(customer.getId());
        return ResponseEntity.ok(cart);
    }

    @PostMapping("/items")
    public ResponseEntity<CartResponseDTO> addItemToCart(
            @RequestBody @Valid AddItemToCartDTO itemDTO,
            @AuthenticationPrincipal User customer) {

        CartResponseDTO cart = cartService.addItemToCart(customer.getId(), itemDTO);
        return ResponseEntity.ok(cart);
    }

    @DeleteMapping("/items/{itemId}")
    public ResponseEntity<CartResponseDTO> removeItem(
            @PathVariable Long itemId,
            @AuthenticationPrincipal User customer) {

        CartResponseDTO cart = cartService.removeItemFromCart(customer.getId(), itemId);
        return ResponseEntity.ok(cart);
    }

    @PutMapping("/items/{itemId}")
    public ResponseEntity<CartResponseDTO> updateItemQuantity(
            @PathVariable Long itemId,
            @RequestBody @Valid UpdateItemQuantityDTO quantityDTO,
            @AuthenticationPrincipal User customer) {

        CartResponseDTO cart = cartService.updateItemQuantity(customer.getId(), itemId, quantityDTO.getNewQuantity());
        return ResponseEntity.ok(cart);
    }
}