package br.com.iff.marketplace.controller.dto;

import br.com.iff.marketplace.model.Order;
import br.com.iff.marketplace.model.enums.StatusPedido;
import lombok.Data;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@Data
public class PedidoResponseDTO {
    private String numeroPedido;
    private LocalDateTime dataPedido;
    private BigDecimal valorTotal;
    private StatusPedido status;
    private String compradorNome;
    private List<ItemPedidoResponseDTO> itens;

    public PedidoResponseDTO(Order pedido) {
        this.numeroPedido = pedido.getNumeroPedido();
        this.dataPedido = pedido.getDataPedido();
        this.valorTotal = pedido.getValorTotal();
        this.status = pedido.getStatus();
        this.compradorNome = pedido.getComprador().getName();

        this.itens = pedido.getItens().stream()
                .map(ItemPedidoResponseDTO::new)
                .collect(Collectors.toList());
    }
}