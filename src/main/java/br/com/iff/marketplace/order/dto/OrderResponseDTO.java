package br.com.iff.marketplace.order.dto;

import br.com.iff.marketplace.order.Order;
import br.com.iff.marketplace.order.enums.OrderStatus;
import lombok.Data;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@Data
public class OrderResponseDTO {
    private String numeroPedido;
    private LocalDateTime dataPedido;
    private BigDecimal valorTotal;
    private OrderStatus status;
    private String compradorNome;
    private List<OrderItemResponseDTO> itens;

    public OrderResponseDTO(Order pedido) {
        this.numeroPedido = pedido.getOrderNumber();
        this.dataPedido = pedido.getOrderDate();
        this.valorTotal = pedido.getTotalAmount();
        this.status = pedido.getStatus();
        this.compradorNome = pedido.getCustomer().getName();

        this.itens = pedido.getItems().stream()
                .map(OrderItemResponseDTO::new)
                .collect(Collectors.toList());
    }
}