package br.com.iff.marketplace.order.dto;

import jakarta.validation.constraints.NotNull;
import lombok.Data;

@Data
public class AddTrackingDTO {

    @NotNull(message = "O código de rastreio nao pode ser nulo")
    private String trackingCode;

}
