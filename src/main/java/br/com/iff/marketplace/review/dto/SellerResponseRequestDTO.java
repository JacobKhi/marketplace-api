package br.com.iff.marketplace.review.dto;

import jakarta.validation.constraints.NotBlank;
import lombok.Data;

@Data
public class SellerResponseRequestDTO {

    @NotBlank(message = "A resposta não pode estar vazia")
    private String response;

}
