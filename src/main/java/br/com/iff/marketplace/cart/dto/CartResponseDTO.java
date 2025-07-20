package br.com.iff.marketplace.cart.dto;

import br.com.iff.marketplace.cart.ShoppingCart;
import lombok.Data;

import java.math.BigDecimal;
import java.util.List;
import java.util.stream.Collectors;

@Data
public class CartResponseDTO {

    private Long cartId;
    private List<CartItemResponseDTO> items;
    private BigDecimal totalAmount;

    public CartResponseDTO(ShoppingCart carrinho) {
        this.cartId = carrinho.getId();
        this.items = carrinho.getItems().stream()
                .map(CartItemResponseDTO::new)
                .collect(Collectors.toList());
    }
}