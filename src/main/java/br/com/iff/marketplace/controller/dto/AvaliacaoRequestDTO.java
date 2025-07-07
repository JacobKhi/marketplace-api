package br.com.iff.marketplace.controller.dto;

import lombok.Data;

@Data
public class AvaliacaoRequestDTO {
    private Long pedidoId;
    private Long avaliadorId;
    private Integer nota;
    private String comentario;
}