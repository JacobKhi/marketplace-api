package br.com.iff.marketplace.order.dto;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import lombok.Data;

@Data
public class OrderItemRequestDTO {

    @NotNull(message = "O Id da variacao nao pode ser nulo")
    private Long variationId;

    @NotNull(message = "A quantidade de produtos nao pode ser nula")
    @Positive(message = "A quantidade de produtos deve ser positiva")
    private Integer quantity;

}