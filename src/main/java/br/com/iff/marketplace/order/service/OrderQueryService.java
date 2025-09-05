package br.com.iff.marketplace.order.service;

import br.com.iff.marketplace.exception.NotFoundException;
import br.com.iff.marketplace.order.Order;
import br.com.iff.marketplace.order.dto.OrderResponseDTO;
import br.com.iff.marketplace.order.repository.OrderRepository;
import br.com.iff.marketplace.user.User;
import br.com.iff.marketplace.user.enums.UserProfiles;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class OrderQueryService {

    private final OrderRepository orderRepository;

    public Page<OrderResponseDTO> findAllOrdersForUser(
            User authenticatedUser,
            Pageable pageable) {

        Page<Order> ordersPage;

        if (authenticatedUser.getProfile() == UserProfiles.CUSTOMER) {
            ordersPage = orderRepository.findByCustomerId(authenticatedUser.getId(), pageable);
        }
        else if (authenticatedUser.getProfile() == UserProfiles.SELLER) {
            ordersPage = orderRepository.findBySellerId(authenticatedUser.getId(), pageable);
        }
        else {
            ordersPage = orderRepository.findAll(pageable);
        }

        return ordersPage.map(OrderResponseDTO::new);
    }

    public OrderResponseDTO findOrderById(
            Long orderId,
            User authenticatedUser) {

        Order order = orderRepository.findById(orderId)
                .orElseThrow(() -> new NotFoundException("Pedido com ID " + orderId + " não encontrado!"));

        boolean isTheCustomer = order.getCustomer().getId().equals(authenticatedUser.getId());

        boolean isSellerOfAnItem = order.getItems().stream()
                .anyMatch(item -> item.getProduct().getSeller().getId().equals(authenticatedUser.getId()));

        if (isTheCustomer || isSellerOfAnItem || authenticatedUser.getProfile() == UserProfiles.ADMIN) {
            return new OrderResponseDTO(order);
        }
        else {
            throw new AccessDeniedException("Você não tem permissão para visualizar este pedido.");
        }
    }

}