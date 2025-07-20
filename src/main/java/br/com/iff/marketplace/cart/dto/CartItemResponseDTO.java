package br.com.iff.marketplace.cart.dto;

import br.com.iff.marketplace.cart.ShoppingCartItem;
import lombok.Data;
import java.math.BigDecimal;

@Data
public class CartItemResponseDTO {

    private Long itemId;
    private Long productId;
    private String productName;
    private Integer quantity;
    private BigDecimal unitPrice;

    public CartItemResponseDTO(ShoppingCartItem item) {
        this.itemId = item.getId();
        this.productId = item.getVariation().getProduct().getId();
        this.productName = item.getVariation().getProduct().getName();
        this.quantity = item.getQuantity();
        this.unitPrice = item.getVariation().getPrice();
    }
}