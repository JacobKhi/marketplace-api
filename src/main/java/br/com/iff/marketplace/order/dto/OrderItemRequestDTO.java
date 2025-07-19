package br.com.iff.marketplace.order.dto;

import lombok.Data;

@Data
public class OrderItemRequestDTO {
    private Long variacaoId;
    private Integer quantidade;
}