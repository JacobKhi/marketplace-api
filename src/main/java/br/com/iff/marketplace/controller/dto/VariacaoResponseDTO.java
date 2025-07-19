package br.com.iff.marketplace.controller.dto;

import br.com.iff.marketplace.product.ProductVariation;
import lombok.Data;
import java.math.BigDecimal;

@Data
public class VariacaoResponseDTO {
    private Long id;
    private String nome;
    private String sku;
    private BigDecimal preco;
    private Integer estoque;

    public VariacaoResponseDTO(ProductVariation variacao) {
        this.id = variacao.getId();
        this.nome = variacao.getName();
        this.sku = variacao.getSku();
        this.preco = variacao.getPrice();
        this.estoque = variacao.getStock();
    }
}