package br.com.iff.marketplace.controller;

import br.com.iff.marketplace.controller.dto.PedidoRequestDTO;
import br.com.iff.marketplace.controller.dto.PedidoResponseDTO;
import br.com.iff.marketplace.model.Pedido;
import br.com.iff.marketplace.service.PedidoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.util.List;

@RestController
@RequestMapping("/pedidos")
public class PedidoController {

    @Autowired
    private PedidoService service;

    @PostMapping
    public ResponseEntity<PedidoResponseDTO> criarPedido(@RequestBody PedidoRequestDTO pedidoDTO) {
        Pedido novoPedido = service.criarPedido(pedidoDTO);
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
}