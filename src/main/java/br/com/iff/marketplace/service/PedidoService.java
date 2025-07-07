package br.com.iff.marketplace.service;

import br.com.iff.marketplace.controller.dto.PedidoRequestDTO;
import br.com.iff.marketplace.controller.dto.PedidoResponseDTO;
import br.com.iff.marketplace.controller.dto.ItemPedidoRequestDTO;
import br.com.iff.marketplace.model.*;
import br.com.iff.marketplace.model.enums.StatusPedido;
import br.com.iff.marketplace.repository.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors; // Importe o Collectors

@Service
public class PedidoService {

    @Autowired
    private PedidoRepository pedidoRepository;
    @Autowired
    private ProdutoRepository produtoRepository;
    @Autowired
    private UsuarioRepository usuarioRepository;

    @Transactional
    public Pedido criarPedido(PedidoRequestDTO dto) {
        Usuario comprador = usuarioRepository.findById(dto.getCompradorId())
                .orElseThrow(() -> new RuntimeException("Comprador não encontrado!"));

        Pedido pedido = new Pedido();
        pedido.setComprador(comprador);
        pedido.setDataPedido(LocalDateTime.now());
        pedido.setStatus(StatusPedido.PROCESSANDO);
        pedido.setNumeroPedido(UUID.randomUUID().toString());

        List<ItemPedido> itensPedido = new ArrayList<>();
        BigDecimal valorTotal = BigDecimal.ZERO;

        for (ItemPedidoRequestDTO itemDTO : dto.getItens()) {
            Produto produto = produtoRepository.findById(itemDTO.getProdutoId())
                    .orElseThrow(() -> new RuntimeException("Produto não encontrado!"));

            if (produto.getEstoque() < itemDTO.getQuantidade()) {
                throw new RuntimeException("Estoque insuficiente para o produto: " + produto.getNome());
            }

            ItemPedido itemPedido = new ItemPedido();
            itemPedido.setProduto(produto);
            itemPedido.setQuantidade(itemDTO.getQuantidade());
            itemPedido.setPrecoUnitario(produto.getPreco());
            itemPedido.setPedido(pedido);
            itensPedido.add(itemPedido);

            produto.setEstoque(produto.getEstoque() - itemDTO.getQuantidade());
            produtoRepository.save(produto);

            valorTotal = valorTotal.add(produto.getPreco().multiply(new BigDecimal(itemDTO.getQuantidade())));
        }

        pedido.setItens(itensPedido);
        pedido.setValorTotal(valorTotal);

        return pedidoRepository.save(pedido);
    }

    public List<PedidoResponseDTO> listarPedidos() {
        return pedidoRepository.findAll().stream()
                .map(PedidoResponseDTO::new)
                .collect(Collectors.toList());
    }

    public PedidoResponseDTO buscarPorId(Long id) {
        Pedido pedido = pedidoRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Pedido não encontrado!"));
        return new PedidoResponseDTO(pedido);
    }
}