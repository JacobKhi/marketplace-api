package br.com.iff.marketplace.category.dto;

import br.com.iff.marketplace.category.Category;
import jakarta.validation.constraints.NotBlank;
import lombok.Data;

@Data
public class CategoryResponseDTO {

    private Long id;
    private String name;

    public CategoryResponseDTO(Category category) {

        this.id = category.getId();
        this.name = category.getName();

    }

}
