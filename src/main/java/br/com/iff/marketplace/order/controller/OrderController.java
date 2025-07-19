package br.com.iff.marketplace.order.controller;

import br.com.iff.marketplace.order.Order;
import br.com.iff.marketplace.order.dto.OrderRequestDTO;
import br.com.iff.marketplace.order.dto.OrderResponseDTO;
import br.com.iff.marketplace.service.PedidoService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.util.List;
import br.com.iff.marketplace.order.enums.OrderStatus;
import java.util.Map;

@RestController
@RequestMapping("/pedidos")
@RequiredArgsConstructor
public class OrderController {

    private final PedidoService service;

    @PostMapping
    public ResponseEntity<OrderResponseDTO> criarPedido(@RequestBody OrderRequestDTO pedidoDTO) {
        Order novoPedido = service.criarPedido(pedidoDTO);
        return ResponseEntity.ok(new OrderResponseDTO(novoPedido));
    }

    @GetMapping
    public ResponseEntity<List<OrderResponseDTO>> listarPedidos() {
        return ResponseEntity.ok(service.listarPedidos());
    }

    @GetMapping("/{id}")
    public ResponseEntity<OrderResponseDTO> buscarPedidoPorId(@PathVariable Long id) {
        return ResponseEntity.ok(service.buscarPorId(id));
    }

    @PatchMapping("/{id}/status")
    public ResponseEntity<OrderResponseDTO> atualizarStatusPedido(
            @PathVariable Long id,
            @RequestBody Map<String, String> body) {

        OrderStatus novoStatus = OrderStatus.valueOf(body.get("status").toUpperCase());

        Order pedidoAtualizado = service.atualizarStatusPedido(id, novoStatus);
        return ResponseEntity.ok(new OrderResponseDTO(pedidoAtualizado));
    }

    @PatchMapping("/{id}/rastreio")
    public ResponseEntity<OrderResponseDTO> adicionarCodigoRastreio(
            @PathVariable Long id,
            @RequestBody Map<String, String> body) {

        String codigoRastreio = body.get("codigoRastreio");
        Order pedidoAtualizado = service.adicionarCodigoRastreio(id, codigoRastreio);
        return ResponseEntity.ok(new OrderResponseDTO(pedidoAtualizado));
    }

    @PostMapping("/checkout")
    public ResponseEntity<OrderResponseDTO> checkout() {
        Order novoPedido = service.criarPedidoAPartirDoCarrinho();

        return ResponseEntity.ok(new OrderResponseDTO(novoPedido));
    }

}