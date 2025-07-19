package br.com.iff.marketplace.service;

import br.com.iff.marketplace.controller.dto.PedidoRequestDTO;
import br.com.iff.marketplace.controller.dto.PedidoResponseDTO;
import br.com.iff.marketplace.controller.dto.ItemPedidoRequestDTO;
import br.com.iff.marketplace.model.*;
import br.com.iff.marketplace.model.enums.PerfilUsuario;
import br.com.iff.marketplace.model.enums.StatusPedido;
import br.com.iff.marketplace.product.ProductRepository;
import br.com.iff.marketplace.repository.*;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.security.core.context.SecurityContextHolder;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;
import br.com.iff.marketplace.model.CarrinhoDeCompras;
import br.com.iff.marketplace.model.CarrinhoDeComprasItem;
import br.com.iff.marketplace.repository.CarrinhoDeComprasRepository;

@Service
@RequiredArgsConstructor
public class PedidoService {

    private final PedidoRepository pedidoRepository;

    private final ProductRepository productRepository;

    private final UsuarioRepository usuarioRepository;

    private final VariacaoProdutoRepository variacaoProdutoRepository;

    private final CarrinhoDeComprasRepository carrinhoRepository;

    public List<PedidoResponseDTO> listarPedidos() {
        User userLogado = (User) SecurityContextHolder.getContext().getAuthentication().getPrincipal();

        List<Order> pedidos;

        if (userLogado.getProfile() == PerfilUsuario.COMPRADOR) {
            pedidos = pedidoRepository.findByCompradorId(userLogado.getId());
        } else if (userLogado.getProfile() == PerfilUsuario.VENDEDOR) {
            pedidos = pedidoRepository.findByVendedorId(userLogado.getId());
        } else {
            pedidos = pedidoRepository.findAll();
        }

        return pedidos.stream()
                .map(PedidoResponseDTO::new)
                .collect(Collectors.toList());
    }

    public PedidoResponseDTO buscarPorId(Long id) {
        Order pedido = pedidoRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Pedido não encontrado!"));
        return new PedidoResponseDTO(pedido);
    }

    @Transactional
    public Order criarPedido(PedidoRequestDTO dto) {
        User comprador = usuarioRepository.findById(dto.getCompradorId())
                .orElseThrow(() -> new RuntimeException("Comprador não encontrado!"));

        Order pedido = new Order();
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

    private Order verificaVendedorDoPedido(Long pedidoId) {
        User userLogado = (User) SecurityContextHolder.getContext().getAuthentication().getPrincipal();

        Order pedidoEncontrado = pedidoRepository.findById(pedidoId)
                .orElseThrow(() -> new RuntimeException("Pedido não encontrado!"));

        boolean isVendedorDoPedido = pedidoEncontrado.getItens().stream()
                .anyMatch(item -> item.getProduto().getSeller().getId().equals(userLogado.getId()));

        if (!isVendedorDoPedido) {
            throw new RuntimeException("Acesso negado: Você não é o vendedor de nenhum item neste pedido.");
        }

        return pedidoEncontrado; // Retorna o pedido se a verificação passar
    }

    @Transactional
    public Order adicionarCodigoRastreio(Long id, String codigoRastreio) {
        Order pedidoEncontrado = verificaVendedorDoPedido(id);
        pedidoEncontrado.setCodigoRastreio(codigoRastreio);
        return pedidoRepository.save(pedidoEncontrado);
    }

    @Transactional
    public Order atualizarStatusPedido(Long id, StatusPedido novoStatus) {
        Order pedidoEncontrado = verificaVendedorDoPedido(id);
        pedidoEncontrado.setStatus(novoStatus);
        return pedidoRepository.save(pedidoEncontrado);
    }

    @Transactional
    public Order criarPedidoAPartirDoCarrinho() {
        User comprador = (User) SecurityContextHolder.getContext().getAuthentication().getPrincipal();

        CarrinhoDeCompras carrinho = carrinhoRepository.findByUsuarioId(comprador.getId())
                .orElseThrow(() -> new RuntimeException("Usuário não possui um carrinho de compras."));

        if (carrinho.getItens() == null || carrinho.getItens().isEmpty()) {
            throw new RuntimeException("Seu carrinho está vazio!");
        }

        Order pedido = new Order();
        pedido.setComprador(comprador);
        pedido.setDataPedido(LocalDateTime.now());
        pedido.setStatus(StatusPedido.PROCESSANDO);
        pedido.setNumeroPedido(UUID.randomUUID().toString());

        List<ItemPedido> itensPedido = new ArrayList<>();
        BigDecimal valorTotal = BigDecimal.ZERO;

        for (CarrinhoDeComprasItem itemCarrinho : carrinho.getItens()) {
            VariacaoProduto variacao = itemCarrinho.getVariacao();

            if (variacao.getEstoque() < itemCarrinho.getQuantidade()) {
                throw new RuntimeException("Estoque insuficiente para o produto: " + variacao.getNome());
            }

            ItemPedido itemPedido = new ItemPedido();
            itemPedido.setProduto(variacao.getProduto());
            itemPedido.setQuantidade(itemCarrinho.getQuantidade());
            itemPedido.setPrecoUnitario(variacao.getPreco());
            itemPedido.setPedido(pedido);
            itensPedido.add(itemPedido);

            variacao.setEstoque(variacao.getEstoque() - itemCarrinho.getQuantidade());
            variacaoProdutoRepository.save(variacao);

            valorTotal = valorTotal.add(variacao.getPreco().multiply(new BigDecimal(itemCarrinho.getQuantidade())));
        }

        pedido.setItens(itensPedido);
        pedido.setValorTotal(valorTotal);

        Order pedidoSalvo = pedidoRepository.save(pedido);

        carrinho.getItens().clear();
        carrinhoRepository.save(carrinho);

        return pedidoSalvo;
    }

}