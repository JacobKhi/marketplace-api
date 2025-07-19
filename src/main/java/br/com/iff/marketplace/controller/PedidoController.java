package br.com.iff.marketplace.controller;

import br.com.iff.marketplace.controller.dto.PedidoRequestDTO;
import br.com.iff.marketplace.controller.dto.PedidoResponseDTO;
import br.com.iff.marketplace.model.Order;
import br.com.iff.marketplace.service.PedidoService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.util.List;
import br.com.iff.marketplace.model.enums.StatusPedido;
import java.util.Map;

@RestController
@RequestMapping("/pedidos")
@RequiredArgsConstructor
public class PedidoController {

    private final PedidoService service;

    @PostMapping
    public ResponseEntity<PedidoResponseDTO> criarPedido(@RequestBody PedidoRequestDTO pedidoDTO) {
        Order novoPedido = service.criarPedido(pedidoDTO);
        return ResponseEntity.ok(new PedidoResponseDTO(novoPedido));
    }

    @GetMapping
    public ResponseEntity<List<PedidoResponseDTO>> listarPedidos() {
        return ResponseEntity.ok(service.listarPedidos());
    }

    @GetMapping("/{id}")
    public ResponseEntity<PedidoResponseDTO> buscarPedidoPorId(@PathVariable Long id) {
        return ResponseEntity.ok(service.buscarPorId(id));
    }

    @PatchMapping("/{id}/status")
    public ResponseEntity<PedidoResponseDTO> atualizarStatusPedido(
            @PathVariable Long id,
            @RequestBody Map<String, String> body) {

        StatusPedido novoStatus = StatusPedido.valueOf(body.get("status").toUpperCase());

        Order pedidoAtualizado = service.atualizarStatusPedido(id, novoStatus);
        return ResponseEntity.ok(new PedidoResponseDTO(pedidoAtualizado));
    }

    @PatchMapping("/{id}/rastreio")
    public ResponseEntity<PedidoResponseDTO> adicionarCodigoRastreio(
            @PathVariable Long id,
            @RequestBody Map<String, String> body) {

        String codigoRastreio = body.get("codigoRastreio");
        Order pedidoAtualizado = service.adicionarCodigoRastreio(id, codigoRastreio);
        return ResponseEntity.ok(new PedidoResponseDTO(pedidoAtualizado));
    }

    @PostMapping("/checkout")
    public ResponseEntity<PedidoResponseDTO> checkout() {
        Order novoPedido = service.criarPedidoAPartirDoCarrinho();

        return ResponseEntity.ok(new PedidoResponseDTO(novoPedido));
    }

}