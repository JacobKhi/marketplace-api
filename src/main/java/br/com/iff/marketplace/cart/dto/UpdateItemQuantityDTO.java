package br.com.iff.marketplace.cart.dto;

import jakarta.validation.constraints.Positive;
import lombok.Data;

@Data
public class UpdateItemQuantityDTO {

    @Positive(message = "A nova quantidade deve ser um número positivo.")
    private Integer newQuantity;

}
