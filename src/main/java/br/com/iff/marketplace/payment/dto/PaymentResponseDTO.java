package br.com.iff.marketplace.payment.dto;

import lombok.Data;

@Data
public class PaymentResponseDTO {

    private String chargeId;

    private String status;

    private String orderNumber;

    public PaymentResponseDTO(String chargeId, String status, String orderNumber) {
        this.chargeId = chargeId;
        this.status = status;
        this.orderNumber = orderNumber;
    }
}
