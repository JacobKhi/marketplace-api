package br.com.iff.marketplace.product.dto;

import lombok.Data;

@Data
public class ProductRequestDTO {

    private String nome;
    private String descricao;

    private Long categoriaId;
    private Long vendedorId;
}