package br.com.iff.marketplace.order.service;

import br.com.iff.marketplace.exception.NotFoundException;
import br.com.iff.marketplace.order.Order;
import br.com.iff.marketplace.order.dto.OrderResponseDTO;
import br.com.iff.marketplace.order.enums.OrderStatus;
import br.com.iff.marketplace.order.repository.OrderRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class SellerOrderService {

    private final OrderRepository orderRepository;

    @Transactional
    public OrderResponseDTO addTrackingCode(
            Long orderId,
            String trackingCode,
            Long sellerId) {

        Order foundOrder = findAndAuthorizeSellerForOrder(orderId, sellerId);
        foundOrder.setTrackingCode(trackingCode);
        Order savedOrder = orderRepository.save(foundOrder);

        return new OrderResponseDTO(savedOrder);
    }

    @Transactional
    public OrderResponseDTO updateOrderStatus(
            Long orderId,
            OrderStatus newStatus,
            Long sellerId) {

        Order foundOrder = findAndAuthorizeSellerForOrder(orderId, sellerId);
        foundOrder.setStatus(newStatus);
        Order updatedOrder = orderRepository.save(foundOrder);

        return new OrderResponseDTO(updatedOrder);
    }

    private Order findAndAuthorizeSellerForOrder(
            Long orderId,
            Long sellerId) {

        Order order = orderRepository.findById(orderId)
                .orElseThrow(() -> new NotFoundException("Pedido de ID " + orderId + " não encontrado!"));

        boolean isSellerOfAnItem = order.getItems().stream()
                .anyMatch(item -> item.getProduct().getSeller().getId().equals(sellerId));

        if (!isSellerOfAnItem) {
            throw new AccessDeniedException("Acesso negado: Você não é o vendedor de nenhum item neste pedido.");
        }

        return order;
    }

}