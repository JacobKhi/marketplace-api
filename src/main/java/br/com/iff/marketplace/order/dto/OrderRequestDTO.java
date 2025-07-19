package br.com.iff.marketplace.order.dto;

import java.util.List;

import jakarta.validation.Valid;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

@Data
public class OrderRequestDTO {

    @NotNull(message = "O ID do endereço de entrega é obrigatório.")
    private Long addressId;

    @NotEmpty(message = "O pedido deve conter pelo menos um item.")
    @Valid
    private List<OrderItemRequestDTO> items;

}