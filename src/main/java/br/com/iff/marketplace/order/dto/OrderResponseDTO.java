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

    private Long id;

    private String orderNumber;

    private LocalDateTime orderDate;

    private BigDecimal totalAmount;

    private OrderStatus status;

    private String customerName;

    private List<OrderItemResponseDTO> items;

    public OrderResponseDTO(Order pedido) {
        this.id = pedido.getId();
        this.orderNumber = pedido.getOrderNumber();
        this.orderDate = pedido.getOrderDate();
        this.totalAmount = pedido.getTotalAmount();
        this.status = pedido.getStatus();
        this.customerName = pedido.getCustomer().getName();

        this.items = pedido.getItems().stream()
                .map(OrderItemResponseDTO::new)
                .collect(Collectors.toList());
    }
}