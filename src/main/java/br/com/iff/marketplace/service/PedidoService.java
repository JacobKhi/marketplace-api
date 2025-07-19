package br.com.iff.marketplace.service;

import br.com.iff.marketplace.order.*;
import br.com.iff.marketplace.order.dto.OrderItemRequestDTO;
import br.com.iff.marketplace.order.dto.OrderRequestDTO;
import br.com.iff.marketplace.order.dto.OrderResponseDTO;
import br.com.iff.marketplace.order.repository.OrderRepository;
import br.com.iff.marketplace.user.enums.UserProfiles;
import br.com.iff.marketplace.order.enums.OrderStatus;
import br.com.iff.marketplace.product.repository.ProductRepository;
import br.com.iff.marketplace.product.ProductVariation;
import br.com.iff.marketplace.product.repository.ProductVariationRepository;
import br.com.iff.marketplace.user.User;
import br.com.iff.marketplace.user.repository.UserRepository;
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

    private final OrderRepository orderRepository;

    private final ProductRepository productRepository;

    private final UserRepository userRepository;

    private final ProductVariationRepository productVariationRepository;

    private final CarrinhoDeComprasRepository carrinhoRepository;

    public List<OrderResponseDTO> listarPedidos() {
        User userLogado = (User) SecurityContextHolder.getContext().getAuthentication().getPrincipal();

        List<Order> pedidos;

        if (userLogado.getProfile() == UserProfiles.CUSTOMER) {
            pedidos = orderRepository.findByCompradorId(userLogado.getId());
        } else if (userLogado.getProfile() == UserProfiles.SELLER) {
            pedidos = orderRepository.findByVendedorId(userLogado.getId());
        } else {
            pedidos = orderRepository.findAll();
        }

        return pedidos.stream()
                .map(OrderResponseDTO::new)
                .collect(Collectors.toList());
    }

    public OrderResponseDTO buscarPorId(Long id) {
        Order pedido = orderRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Pedido não encontrado!"));
        return new OrderResponseDTO(pedido);
    }

    @Transactional
    public Order criarPedido(OrderRequestDTO dto) {
        User comprador = userRepository.findById(dto.getCustomerId())
                .orElseThrow(() -> new RuntimeException("Comprador não encontrado!"));

        Order pedido = new Order();
        pedido.setCustomer(comprador);
        pedido.setOrderDate(LocalDateTime.now());
        pedido.setStatus(OrderStatus.PROCESSING);
        pedido.setOrderNumber(UUID.randomUUID().toString());

        List<OrderItem> itensPedido = new ArrayList<>();
        BigDecimal valorTotal = BigDecimal.ZERO;

        for (OrderItemRequestDTO itemDTO : dto.getItems()) {
            ProductVariation variacao = productVariationRepository.findById(itemDTO.getVariationId())
                    .orElseThrow(() -> new RuntimeException("Variação de produto não encontrada!"));

            if (variacao.getStock() < itemDTO.getQuantity()) {
                throw new RuntimeException("Estoque insuficiente para a variação: " + variacao.getName());
            }

            OrderItem orderItem = new OrderItem();
            orderItem.setProduct(variacao.getProduct());
            orderItem.setQuantity(itemDTO.getQuantity());
            orderItem.setUnitPrice(variacao.getPrice());
            orderItem.setOrder(pedido);
            itensPedido.add(orderItem);

            variacao.setStock(variacao.getStock() - itemDTO.getQuantity());
            productVariationRepository.save(variacao);

            valorTotal = valorTotal.add(variacao.getPrice().multiply(new BigDecimal(itemDTO.getQuantity())));
        }

        pedido.setItems(itensPedido);
        pedido.setTotalAmount(valorTotal);

        return orderRepository.save(pedido);
    }

    private Order verificaVendedorDoPedido(Long pedidoId) {
        User userLogado = (User) SecurityContextHolder.getContext().getAuthentication().getPrincipal();

        Order pedidoEncontrado = orderRepository.findById(pedidoId)
                .orElseThrow(() -> new RuntimeException("Pedido não encontrado!"));

        boolean isVendedorDoPedido = pedidoEncontrado.getItems().stream()
                .anyMatch(item -> item.getProduct().getSeller().getId().equals(userLogado.getId()));

        if (!isVendedorDoPedido) {
            throw new RuntimeException("Acesso negado: Você não é o vendedor de nenhum item neste pedido.");
        }

        return pedidoEncontrado; // Retorna o pedido se a verificação passar
    }

    @Transactional
    public Order adicionarCodigoRastreio(Long id, String codigoRastreio) {
        Order pedidoEncontrado = verificaVendedorDoPedido(id);
        pedidoEncontrado.setTrackingCode(codigoRastreio);
        return orderRepository.save(pedidoEncontrado);
    }

    @Transactional
    public Order atualizarStatusPedido(Long id, OrderStatus novoStatus) {
        Order pedidoEncontrado = verificaVendedorDoPedido(id);
        pedidoEncontrado.setStatus(novoStatus);
        return orderRepository.save(pedidoEncontrado);
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
        pedido.setCustomer(comprador);
        pedido.setOrderDate(LocalDateTime.now());
        pedido.setStatus(OrderStatus.PROCESSING);
        pedido.setOrderNumber(UUID.randomUUID().toString());

        List<OrderItem> itensPedido = new ArrayList<>();
        BigDecimal valorTotal = BigDecimal.ZERO;

        for (CarrinhoDeComprasItem itemCarrinho : carrinho.getItens()) {
            ProductVariation variacao = itemCarrinho.getVariacao();

            if (variacao.getStock() < itemCarrinho.getQuantidade()) {
                throw new RuntimeException("Estoque insuficiente para o produto: " + variacao.getName());
            }

            OrderItem orderItem = new OrderItem();
            orderItem.setProduct(variacao.getProduct());
            orderItem.setQuantity(itemCarrinho.getQuantidade());
            orderItem.setUnitPrice(variacao.getPrice());
            orderItem.setOrder(pedido);
            itensPedido.add(orderItem);

            variacao.setStock(variacao.getStock() - itemCarrinho.getQuantidade());
            productVariationRepository.save(variacao);

            valorTotal = valorTotal.add(variacao.getPrice().multiply(new BigDecimal(itemCarrinho.getQuantidade())));
        }

        pedido.setItems(itensPedido);
        pedido.setTotalAmount(valorTotal);

        Order pedidoSalvo = orderRepository.save(pedido);

        carrinho.getItens().clear();
        carrinhoRepository.save(carrinho);

        return pedidoSalvo;
    }

}