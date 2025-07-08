package br.com.iff.marketplace.controller.dto;

import java.math.BigDecimal;
import lombok.Data;

@Data
public class VariacaoRequestDTO {

    // Ex: "Cor: Azul, Tamanho: G" ou "Voltagem: 220v"
    private String nome;

    // SKU (Stock Keeping Unit) - Código único para a variação
    private String sku;

    private BigDecimal preco;

    private Integer estoque;
}