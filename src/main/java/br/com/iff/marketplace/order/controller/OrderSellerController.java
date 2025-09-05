package br.com.iff.marketplace.order.controller;

import br.com.iff.marketplace.order.dto.AddTrackingDTO;
import br.com.iff.marketplace.order.dto.OrderResponseDTO;
import br.com.iff.marketplace.order.dto.UpdateOrderStatusDTO;
import br.com.iff.marketplace.order.service.OrderQueryService;
import br.com.iff.marketplace.order.service.SellerOrderService;
import br.com.iff.marketplace.user.User;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/seller/orders")
@PreAuthorize("hasRole('SELLER')")
@RequiredArgsConstructor
public class OrderSellerController {

    private final SellerOrderService sellerOrderService;
    private final OrderQueryService orderQueryService;

    @GetMapping
    public ResponseEntity<Page<OrderResponseDTO>> listSellerOrders(
            @AuthenticationPrincipal User seller,
            Pageable pageable) {

        Page<OrderResponseDTO> ordersPage = orderQueryService.findAllOrdersForUser(seller, pageable);
        return ResponseEntity.ok(ordersPage);
    }

    @PatchMapping("/{orderId}/status")
    public ResponseEntity<OrderResponseDTO> updateOrderStatus(
            @PathVariable Long orderId,
            @RequestBody @Valid UpdateOrderStatusDTO statusDTO,
            @AuthenticationPrincipal User seller) {

        OrderResponseDTO updatedOrder = sellerOrderService.updateOrderStatus(orderId, statusDTO.getNewStatus(), seller.getId());
        return ResponseEntity.ok(updatedOrder);
    }

    @PatchMapping("/{orderId}/tracking")
    public ResponseEntity<OrderResponseDTO> adicionarCodigoRastreio(
            @PathVariable Long orderId,
            @RequestBody @Valid AddTrackingDTO trackingDTO,
            @AuthenticationPrincipal User seller) {

        OrderResponseDTO updatedOrder = sellerOrderService.addTrackingCode(orderId, trackingDTO.getTrackingCode(), seller.getId());
        return ResponseEntity.ok(updatedOrder);
    }

}
