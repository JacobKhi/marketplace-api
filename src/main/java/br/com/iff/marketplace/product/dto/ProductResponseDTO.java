package br.com.iff.marketplace.product.dto;

import br.com.iff.marketplace.controller.dto.VariacaoResponseDTO;
import br.com.iff.marketplace.product.Product;
import lombok.Data;
import java.util.List;
import java.util.stream.Collectors;


@Data
public class ProductResponseDTO {

    private Long id;
    private String nome;
    private String categoriaNome;
    private String vendedorNome;
    private List<VariacaoResponseDTO> variacoes;

    public ProductResponseDTO(Product produto) {
        this.id = produto.getId();
        this.nome = produto.getName();
        this.categoriaNome = produto.getCategory().getName();
        this.vendedorNome = produto.getSeller().getName();
        this.variacoes = produto.getVariations().stream()
                .map(VariacaoResponseDTO::new) // Para cada variação na lista, cria um VariacaoResponseDTO
                .collect(Collectors.toList()); // E junta tudo numa nova lista
    }
}