package br.com.iff.marketplace.controller.dto;

import br.com.iff.marketplace.model.VariacaoProduto;
import lombok.Data;
import java.math.BigDecimal;

@Data
public class VariacaoResponseDTO {
    private Long id;
    private String nome;
    private String sku;
    private BigDecimal preco;
    private Integer estoque;

    public VariacaoResponseDTO(VariacaoProduto variacao) {
        this.id = variacao.getId();
        this.nome = variacao.getNome();
        this.sku = variacao.getSku();
        this.preco = variacao.getPreco();
        this.estoque = variacao.getEstoque();
    }
}