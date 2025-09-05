package br.com.iff.marketplace.order.controller;

import br.com.iff.marketplace.order.dto.OrderRequestDTO;
import br.com.iff.marketplace.order.dto.OrderResponseDTO;
import br.com.iff.marketplace.order.service.CustomerOrderService;
import br.com.iff.marketplace.order.service.OrderQueryService;
import br.com.iff.marketplace.user.User;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/orders")
@PreAuthorize("hasRole('CUSTOMER')")
@RequiredArgsConstructor
public class OrderCustomerController {

    private final CustomerOrderService customerOrderService;
    private final OrderQueryService orderQueryService;

    @GetMapping
    public ResponseEntity<Page<OrderResponseDTO>> listMyOrders(
            @AuthenticationPrincipal User customer,
            Pageable pageable) {

        Page<OrderResponseDTO> ordersPage = orderQueryService.findAllOrdersForUser(customer, pageable);
        return ResponseEntity.ok(ordersPage);
    }

    @GetMapping("/{orderId}")
    public ResponseEntity<OrderResponseDTO> findMyOrderById(
            @PathVariable Long orderId,
            @AuthenticationPrincipal User customer) {

        OrderResponseDTO ordersPage = orderQueryService.findOrderById(orderId, customer);
        return ResponseEntity.ok(ordersPage);
    }

    @PostMapping("/from-cart")
    public ResponseEntity<OrderResponseDTO> createOrderFromCart(@AuthenticationPrincipal User customer) {

        OrderResponseDTO newOrder = customerOrderService.createOrderFromCart(customer.getId());
        return ResponseEntity.status(HttpStatus.CREATED).body(newOrder);
    }

    @PostMapping
    public ResponseEntity<OrderResponseDTO> createDirectOrder(
            @RequestBody @Valid OrderRequestDTO orderDTO,
            @AuthenticationPrincipal User customer) {

        OrderResponseDTO newOrder = customerOrderService.createDirectOrder(orderDTO, customer.getId());
        return ResponseEntity.status(HttpStatus.CREATED).body(newOrder);
    }

}
