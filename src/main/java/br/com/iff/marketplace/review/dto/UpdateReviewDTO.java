package br.com.iff.marketplace.review.dto;

import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

@Data
public class UpdateReviewDTO {

    @NotNull(message = "A nota não pode ser nula")
    @Min(1)
    @Max(5)
    private Integer rating;

    @NotBlank(message = "O comentário não pode estar vazio")
    private String comment;
}