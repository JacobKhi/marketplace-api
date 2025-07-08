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
import org.springframework.security.core.context.SecurityContextHolder;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
public class PedidoService {

    @Autowired
    private PedidoRepository pedidoRepository;
    @Autowired
    private ProdutoRepository produtoRepository;
    @Autowired
    private UsuarioRepository usuarioRepository;
    @Autowired
    private VariacaoProdutoRepository variacaoProdutoRepository;


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
            VariacaoProduto variacao = variacaoProdutoRepository.findById(itemDTO.getVariacaoId())
                    .orElseThrow(() -> new RuntimeException("Variação de produto não encontrada!"));

            if (variacao.getEstoque() < itemDTO.getQuantidade()) {
                throw new RuntimeException("Estoque insuficiente para a variação: " + variacao.getNome());
            }

            ItemPedido itemPedido = new ItemPedido();
            itemPedido.setProduto(variacao.getProduto());
            itemPedido.setQuantidade(itemDTO.getQuantidade());
            itemPedido.setPrecoUnitario(variacao.getPreco());
            itemPedido.setPedido(pedido);
            itensPedido.add(itemPedido);

            variacao.setEstoque(variacao.getEstoque() - itemDTO.getQuantidade());
            variacaoProdutoRepository.save(variacao);

            valorTotal = valorTotal.add(variacao.getPreco().multiply(new BigDecimal(itemDTO.getQuantidade())));
        }

        pedido.setItens(itensPedido);
        pedido.setValorTotal(valorTotal);

        return pedidoRepository.save(pedido);
    }

    private Pedido verificaVendedorDoPedido(Long pedidoId) {
        Usuario usuarioLogado = (Usuario) SecurityContextHolder.getContext().getAuthentication().getPrincipal();

        Pedido pedidoEncontrado = pedidoRepository.findById(pedidoId)
                .orElseThrow(() -> new RuntimeException("Pedido não encontrado!"));

        boolean isVendedorDoPedido = pedidoEncontrado.getItens().stream()
                .anyMatch(item -> item.getProduto().getVendedor().getId().equals(usuarioLogado.getId()));

        if (!isVendedorDoPedido) {
            throw new RuntimeException("Acesso negado: Você não é o vendedor de nenhum item neste pedido.");
        }

        return pedidoEncontrado; // Retorna o pedido se a verificação passar
    }

    @Transactional
    public Pedido adicionarCodigoRastreio(Long id, String codigoRastreio) {
        Pedido pedidoEncontrado = verificaVendedorDoPedido(id);
        pedidoEncontrado.setCodigoRastreio(codigoRastreio);
        return pedidoRepository.save(pedidoEncontrado);
    }

    @Transactional
    public Pedido atualizarStatusPedido(Long id, StatusPedido novoStatus) {
        Pedido pedidoEncontrado = verificaVendedorDoPedido(id);
        pedidoEncontrado.setStatus(novoStatus);
        return pedidoRepository.save(pedidoEncontrado);
    }

}