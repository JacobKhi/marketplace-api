package br.com.iff.marketplace.order.controller;

import br.com.iff.marketplace.order.Order;
import br.com.iff.marketplace.order.dto.AddTrackingDTO;
import br.com.iff.marketplace.order.dto.OrderResponseDTO;
import br.com.iff.marketplace.order.dto.UpdateOrderStatusDTO;
import br.com.iff.marketplace.order.enums.OrderStatus;
import br.com.iff.marketplace.order.service.OrderService;
import br.com.iff.marketplace.user.User;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/v1/seller/orders")
@PreAuthorize("hasRole('SELLER')")
@RequiredArgsConstructor
public class OrderSellerController {

    private final OrderService orderService;

    @GetMapping
    public ResponseEntity<List<OrderResponseDTO>> listSellerOrders(Authentication authentication) {
        User seller = (User) authentication.getPrincipal();
        List<OrderResponseDTO> orders = orderService.findAllOrdersForUser(seller);
        return ResponseEntity.ok(orders);
    }

    @PatchMapping("/{orderId}/status")
    public ResponseEntity<OrderResponseDTO> updateOrderStatus(
            @PathVariable Long orderId,
            @RequestBody @Valid UpdateOrderStatusDTO statusDTO,
            Authentication authentication) {

        User seller = (User) authentication.getPrincipal();
        OrderResponseDTO updatedOrder = orderService.updateOrderStatus(orderId, statusDTO.getNewStatus(), seller.getId());

        return ResponseEntity.ok(updatedOrder);
    }

    @PatchMapping("/{orderId}/tracking")
    public ResponseEntity<OrderResponseDTO> adicionarCodigoRastreio(
            @PathVariable Long orderId,
            @RequestBody @Valid AddTrackingDTO trackingDTO,
            Authentication authentication) {

        User seller = (User) authentication.getPrincipal();
        OrderResponseDTO updatedOrder = orderService.addTrackingCode(orderId, trackingDTO.getTrackingCode(), seller.getId());

        return ResponseEntity.ok(updatedOrder);
    }

}
