package br.com.iff.marketplace.controller.dto;

import br.com.iff.marketplace.model.CarrinhoDeComprasItem;
import lombok.Data;
import java.math.BigDecimal;

@Data
public class CarrinhoItemResponseDTO {

    private Long variacaoId;
    private String nomeVariacao;
    private Integer quantidade;
    private BigDecimal precoUnitario;

    public CarrinhoItemResponseDTO(CarrinhoDeComprasItem item) {
        this.variacaoId = item.getVariacao().getId();
        this.nomeVariacao = item.getVariacao().getNome();
        this.quantidade = item.getQuantidade();
        this.precoUnitario = item.getVariacao().getPreco();
    }
}