package br.com.iff.marketplace.review.dto;

import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

@Data
public class ReviewRequestDTO {

    @NotNull(message = "O id do produto não pode ser nulo")
    private Long productId;

    @NotNull(message = "O id do pedido não pode ser nulo")
    private Long orderId;

    @NotNull(message = "A nota não pode ser nula")
    @Min(1)
    @Max(5)
    private Integer rating;

    @NotBlank(message = "A mensagem não pode estar vazia")
    private String comment;
}