package br.com.iff.marketplace.controller.dto;

import br.com.iff.marketplace.model.CarrinhoDeCompras;
import lombok.Data;
import java.util.List;
import java.util.stream.Collectors;

@Data
public class CarrinhoResponseDTO {

    private Long id;
    private String nomeUsuario;
    private List<CarrinhoItemResponseDTO> itens;

    public CarrinhoResponseDTO(CarrinhoDeCompras carrinho) {
        this.id = carrinho.getId();
        this.nomeUsuario = carrinho.getUsuario().getNome();
        this.itens = carrinho.getItens().stream()
                .map(CarrinhoItemResponseDTO::new)
                .collect(Collectors.toList());
    }
}