package br.com.iff.marketplace.controller.dto;

import br.com.iff.marketplace.model.Produto;
import lombok.Data;
import java.util.List;
import java.util.stream.Collectors;


@Data
public class ProdutoResponseDTO {

    private Long id;
    private String nome;
    private String categoriaNome;
    private String vendedorNome;
    private List<VariacaoResponseDTO> variacoes;

    public ProdutoResponseDTO(Produto produto) {
        this.id = produto.getId();
        this.nome = produto.getNome();
        this.categoriaNome = produto.getCategoria().getNome();
        this.vendedorNome = produto.getVendedor().getNome();
        this.variacoes = produto.getVariacoes().stream()
                .map(VariacaoResponseDTO::new) // Para cada variação na lista, cria um VariacaoResponseDTO
                .collect(Collectors.toList()); // E junta tudo numa nova lista
    }
}