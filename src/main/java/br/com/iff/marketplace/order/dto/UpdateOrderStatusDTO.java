package br.com.iff.marketplace.order.dto;

import br.com.iff.marketplace.order.enums.OrderStatus;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

@Data
public class UpdateOrderStatusDTO {

    @NotNull(message = "O estatus de pedido nao pode ser nulo")
    private OrderStatus newStatus;

}
