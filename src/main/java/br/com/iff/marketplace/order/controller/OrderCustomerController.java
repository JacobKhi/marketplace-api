package br.com.iff.marketplace.order.controller;

import br.com.iff.marketplace.order.Order;
import br.com.iff.marketplace.order.dto.OrderRequestDTO;
import br.com.iff.marketplace.order.dto.OrderResponseDTO;
import br.com.iff.marketplace.order.service.OrderService;
import br.com.iff.marketplace.user.User;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/orders")
@PreAuthorize("hasRole('CUSTOMER')")
@RequiredArgsConstructor
public class OrderCustomerController {

    private final OrderService orderService;

    @GetMapping
    public ResponseEntity<List<OrderResponseDTO>> listMyOrders(Authentication authentication) {

        User customer = (User) authentication.getPrincipal();
        List<OrderResponseDTO> orders = orderService.findAllOrdersForUser(customer);

        return ResponseEntity.ok(orders);
    }

    @GetMapping("/{orderId}")
    public ResponseEntity<OrderResponseDTO> findMyOrderById(
            @PathVariable Long orderId,
            Authentication authentication) {

        User customer = (User) authentication.getPrincipal();
        OrderResponseDTO order = orderService.findOrderById(orderId, customer);

        return ResponseEntity.ok(order);
    }

    @PostMapping("/from-cart")
    public ResponseEntity<OrderResponseDTO> createOrderFromCart(Authentication authentication) {

        User customer = (User) authentication.getPrincipal();
        OrderResponseDTO newOrder = orderService.createOrderFromCart(customer.getId());

        return ResponseEntity.status(HttpStatus.CREATED).body(newOrder);
    }

    @PostMapping
    public ResponseEntity<OrderResponseDTO> createDirectOrder(
            @RequestBody @Valid OrderRequestDTO orderDTO,
            Authentication authentication) {

        User customer = (User) authentication.getPrincipal();
        OrderResponseDTO newOrder = orderService.createDirectOrder(orderDTO, customer.getId());

        return ResponseEntity.status(HttpStatus.CREATED).body(newOrder);
    }



}
