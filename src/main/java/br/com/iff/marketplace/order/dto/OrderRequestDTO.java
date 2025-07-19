package br.com.iff.marketplace.order.dto;

import java.util.List;
import lombok.Data;

@Data
public class OrderRequestDTO {
    private Long compradorId;
    // Futuramente, adicionar o ID do endereço de entrega aqui
    // private Long enderecoId;
    private List<OrderItemRequestDTO> itens;
}