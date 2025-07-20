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
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/cart")
@PreAuthorize("hasRole('CUSTOMER')")
@RequiredArgsConstructor
public class CartController {

    private final CartService cartService;

    // Endpoint para buscar o carrinho de compras do usuário logado
    @GetMapping
    public ResponseEntity<CartResponseDTO> getMyCart(Authentication authentication) {

        User customer = (User) authentication.getPrincipal();

        CartResponseDTO cart = cartService.getCartForUser(customer.getId());

        return ResponseEntity.ok(cart);
    }

    // Endpoint para adicionar um item no carrinho
    @PostMapping("/items")
    public ResponseEntity<CartResponseDTO> addItemToCart(
            @RequestBody @Valid AddItemToCartDTO itemDTO,
            Authentication authentication) {

        User customer = (User) authentication.getPrincipal();

        CartResponseDTO cart = cartService.addItemToCart(customer.getId(), itemDTO);

        return ResponseEntity.ok(cart);
    }

    // Endpoint para remover um item do carrinho do usuário logado
    @DeleteMapping("/items/{itemId}")
    public ResponseEntity<CartResponseDTO> removeItem(@PathVariable Long itemId, Authentication authentication) {

        User customer = (User) authentication.getPrincipal();

        CartResponseDTO cart = cartService.removeItemFromCart(customer.getId(), itemId);

        return ResponseEntity.ok(cart);
    }

    // Endpoint para atualizar a quantidade de um item no carrinho
    @PutMapping("/items/{itemId}")
    public ResponseEntity<CartResponseDTO> updateItemQuantity(
            @PathVariable Long itemId,
            @RequestBody @Valid UpdateItemQuantityDTO quantityDTO,
            Authentication authentication) {

        User customer = (User) authentication.getPrincipal();

        CartResponseDTO cart = cartService.updateItemQuantity(customer.getId(), itemId, quantityDTO.getNewQuantity());

        return ResponseEntity.ok(cart);
    }
}