package br.com.iff.marketplace.cart.dto;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import lombok.Data;

@Data
public class AddItemToCartDTO {

    @NotNull(message = "O id da variacao nao pode ser nulo")
    private Long variationId;

    @NotNull
    @Positive(message = "A quantidade deve ser maior que zero")
    private Integer quantity;

}