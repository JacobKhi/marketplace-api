package br.com.iff.marketplace.order.controller;

import br.com.iff.marketplace.order.Order;
import br.com.iff.marketplace.order.dto.OrderResponseDTO;
import br.com.iff.marketplace.order.enums.OrderStatus;
import br.com.iff.marketplace.order.service.OrderService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RestController
@RequestMapping("/api/v1/seller/orders")
@PreAuthorize("hasRole('SELLER')")
@RequiredArgsConstructor
public class OrderSellerController {

    private final OrderService orderService;

    @PatchMapping("/{id}/status")
    public ResponseEntity<OrderResponseDTO> atualizarStatusPedido(
            @PathVariable Long id,
            @RequestBody Map<String, String> body) {

        OrderStatus novoStatus = OrderStatus.valueOf(body.get("status").toUpperCase());

        Order pedidoAtualizado = orderService.updateOrderStatus(id, novoStatus);
        return ResponseEntity.ok(new OrderResponseDTO(pedidoAtualizado));
    }

    @PatchMapping("/{id}/rastreio")
    public ResponseEntity<OrderResponseDTO> adicionarCodigoRastreio(
            @PathVariable Long id,
            @RequestBody Map<String, String> body) {

        String codigoRastreio = body.get("codigoRastreio");
        Order pedidoAtualizado = orderService.addTrackingCode(id, codigoRastreio);
        return ResponseEntity.ok(new OrderResponseDTO(pedidoAtualizado));
    }

}
