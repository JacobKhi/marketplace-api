package br.com.iff.marketplace.controller.dto;

import br.com.iff.marketplace.model.ItemPedido;
import lombok.Data;
import java.math.BigDecimal;

@Data
public class ItemPedidoResponseDTO {
    private String produtoNome;
    private Integer quantidade;
    private BigDecimal precoUnitario;

    public ItemPedidoResponseDTO(ItemPedido item) {
        this.produtoNome = item.getProduto().getName();
        this.quantidade = item.getQuantidade();
        this.precoUnitario = item.getPrecoUnitario();
    }
}