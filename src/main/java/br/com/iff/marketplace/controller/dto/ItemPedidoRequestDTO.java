package br.com.iff.marketplace.controller.dto;

import lombok.Data;

@Data
public class ItemPedidoRequestDTO {
    private Long produtoId;
    private Integer quantidade;
}