package br.com.iff.marketplace.controller.dto;

import lombok.Data;

@Data
public class PagamentoRequestDTO {

    private String tokenPagamento;
    private Long pedidoId;

}