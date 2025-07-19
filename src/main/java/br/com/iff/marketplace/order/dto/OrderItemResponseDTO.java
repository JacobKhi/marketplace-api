package br.com.iff.marketplace.order.dto;

import br.com.iff.marketplace.order.OrderItem;
import lombok.Data;
import java.math.BigDecimal;

@Data
public class OrderItemResponseDTO {

    private Long productId;

    private String productName;

    private Integer quantity;

    private BigDecimal unitPrice;

    public OrderItemResponseDTO(OrderItem item) {
        this.productId = item.getProduct().getId();
        this.productName = item.getProduct().getName();
        this.quantity = item.getQuantity();
        this.unitPrice = item.getUnitPrice();
    }
}