package br.com.iff.marketplace.product.dto;

import java.math.BigDecimal;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import jakarta.validation.constraints.PositiveOrZero;
import lombok.Data;

@Data
public class ProductVariationRequestDTO {

    @NotBlank(message = "O nome não pode estar em branco")
    private String name;

    private String sku;

    @NotNull(message = "O preco não pode ser nulo")
    @Positive(message = "O preco so pode ser maior que 0")
    private BigDecimal price;

    @NotNull(message = "O estoque não pode ser nulo")
    @PositiveOrZero(message = "O estoque não pode ser negativo")
    private Integer stock;

}