package br.com.iff.marketplace.order.dto;

import br.com.iff.marketplace.order.OrderItem;
import lombok.Data;
import java.math.BigDecimal;

@Data
public class OrderItemResponseDTO {
    private String produtoNome;
    private Integer quantidade;
    private BigDecimal precoUnitario;

    public OrderItemResponseDTO(OrderItem item) {
        this.produtoNome = item.getProduct().getName();
        this.quantidade = item.getQuantity();
        this.precoUnitario = item.getUnitPrice();
    }
}