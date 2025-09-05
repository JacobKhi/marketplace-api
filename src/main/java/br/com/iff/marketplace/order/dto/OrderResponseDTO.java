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

    private String trackingCode;

    private List<OrderItemResponseDTO> items;

    public OrderResponseDTO(Order order) {
        this.id = order.getId();
        this.orderNumber = order.getOrderNumber();
        this.orderDate = order.getOrderDate();
        this.totalAmount = order.getTotalAmount();
        this.status = order.getStatus();
        this.customerName = order.getCustomer().getName();
        this.trackingCode = order.getTrackingCode();

        this.items = order.getItems().stream()
                .map(OrderItemResponseDTO::new)
                .collect(Collectors.toList());
    }
}