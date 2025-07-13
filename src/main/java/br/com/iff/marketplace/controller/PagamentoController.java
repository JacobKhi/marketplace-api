package br.com.iff.marketplace.controller;

import br.com.iff.marketplace.controller.dto.PagamentoRequestDTO;
import br.com.iff.marketplace.model.Pedido;
import br.com.iff.marketplace.model.enums.StatusPedido;
import br.com.iff.marketplace.repository.PedidoRepository;
import br.com.iff.marketplace.service.PagamentoService;
import com.stripe.exception.StripeException;
import com.stripe.model.Charge;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/pagamentos")
@RequiredArgsConstructor
public class PagamentoController {

    private final PagamentoService pagamentoService;
    private final PedidoRepository pedidoRepository;

    @PostMapping
    public ResponseEntity<String> processarPagamento(@RequestBody PagamentoRequestDTO dto) {
        try {
            Pedido pedido = pedidoRepository.findById(dto.getPedidoId())
                    .orElseThrow(() -> new RuntimeException("Pedido não encontrado!"));

            Charge charge = pagamentoService.criarCobranca(
                    dto.getTokenPagamento(),
                    pedido.getValorTotal(),
                    pedido.getId()
            );

            if ("succeeded".equals(charge.getStatus())) {
                pedido.setStatus(StatusPedido.PAGAMENTO_APROVADO);
                pedidoRepository.save(pedido);
                return ResponseEntity.ok("Pagamento aprovado com sucesso! ID da transação: " + charge.getId());
            } else {
                return ResponseEntity.badRequest().body("Pagamento falhou. Status: " + charge.getStatus());
            }

        } catch (StripeException e) {
            return ResponseEntity.badRequest().body("Erro no pagamento: " + e.getMessage());
        } catch (RuntimeException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }
}