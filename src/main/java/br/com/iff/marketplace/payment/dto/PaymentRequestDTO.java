package br.com.iff.marketplace.payment.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

@Data
public class PaymentRequestDTO {

    @NotBlank(message = "O token de pagamento é obrigatório")
    private String paymentToken;

    @NotNull(message = "O id do pedido é obrigatório")
    private Long orderId;

}