package br.com.iff.marketplace.product.dto;

import jakarta.validation.constraints.NotBlank;
import lombok.Data;

@Data
public class UpdateProductDTO {

    @NotBlank(message = "O nome não pode estar em branco")
    private String name;

    private String description;

    private String brand;

    private Boolean sponsoredAd;

}
