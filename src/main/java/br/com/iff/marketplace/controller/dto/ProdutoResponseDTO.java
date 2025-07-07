package br.com.iff.marketplace.controller.dto;

import br.com.iff.marketplace.model.Produto;
import lombok.Data;
import java.math.BigDecimal;

@Data
public class ProdutoResponseDTO {

    private Long id;
    private String nome;
    private BigDecimal preco;
    private Integer estoque;
    private String categoriaNome;
    private String vendedorNome;

    public ProdutoResponseDTO(Produto produto) {
        this.id = produto.getId();
        this.nome = produto.getNome();
        this.preco = produto.getPreco();
        this.estoque = produto.getEstoque();
        this.categoriaNome = produto.getCategoria().getNome();
        this.vendedorNome = produto.getVendedor().getNome();
    }
}