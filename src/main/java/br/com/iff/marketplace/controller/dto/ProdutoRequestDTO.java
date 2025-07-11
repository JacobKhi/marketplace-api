package br.com.iff.marketplace.controller.dto;

import lombok.Data;

@Data
public class ProdutoRequestDTO {

    private String nome;
    private String descricao;

    private Long categoriaId;
    private Long vendedorId;
}