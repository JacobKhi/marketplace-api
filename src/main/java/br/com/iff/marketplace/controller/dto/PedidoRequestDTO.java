package br.com.iff.marketplace.controller.dto;

import java.util.List;
import lombok.Data;

@Data
public class PedidoRequestDTO {
    private Long compradorId;
    // Futuramente, adicionar o ID do endereço de entrega aqui
    // private Long enderecoId;
    private List<ItemPedidoRequestDTO> itens;
}