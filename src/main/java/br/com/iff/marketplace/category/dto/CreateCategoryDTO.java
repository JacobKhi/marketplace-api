package br.com.iff.marketplace.category.dto;

import jakarta.validation.constraints.NotBlank;
import lombok.Data;

@Data
public class CreateCategoryDTO {

    @NotBlank(message = "O nome não pode estar em branco")
    private String name;

    private String description;

}
