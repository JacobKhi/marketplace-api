package br.com.iff.marketplace.controller.dto;

import lombok.Data;

@Data
public class ItemPedidoRequestDTO {
    private Long variacaoId;
    private Integer quantidade;
}