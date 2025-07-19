package br.com.iff.marketplace.order.service;

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
import org.springframework.security.access.AccessDeniedException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

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
public class OrderService {

    private final OrderRepository orderRepository;
    private final ProductRepository productRepository;
    private final UserRepository userRepository;
    private final ProductVariationRepository productVariationRepository;
    private final CarrinhoDeComprasRepository carrinhoRepository;

    public List<OrderResponseDTO> findAllOrdersForUser(User authenticatedUser) {

        List<Order> orders;

        if (authenticatedUser.getProfile() == UserProfiles.CUSTOMER) {
            orders = orderRepository.findByCustomerId(authenticatedUser.getId());
        } else if (authenticatedUser.getProfile() == UserProfiles.SELLER) {
            orders = orderRepository.findBySellerId(authenticatedUser.getId());
        } else {
            orders = orderRepository.findAll();
        }

        return orders.stream()
                .map(OrderResponseDTO::new)
                .collect(Collectors.toList());
    }

    public OrderResponseDTO findOrderById(Long orderId, User authenticatedUser) {

        Order order = orderRepository.findById(orderId)
                .orElseThrow(() -> new RuntimeException("Pedido não encontrado!"));

        boolean isTheCustomer = order.getCustomer().getId().equals(authenticatedUser.getId());

        boolean isSellerOfAnItem = order.getItems().stream()
                .anyMatch(item -> item.getProduct().getSeller().getId().equals(authenticatedUser.getId()));

        if (isTheCustomer || isSellerOfAnItem || authenticatedUser.getProfile() == UserProfiles.ADMIN) {
            return new OrderResponseDTO(order);
        } else {
            throw new AccessDeniedException("Você não tem permissão para visualizar este pedido.");
        }
    }

    @Transactional
    public OrderResponseDTO createDirectOrder(OrderRequestDTO orderDTO, Long customerId) {

        User customer = userRepository.findById(customerId)
                .orElseThrow(() -> new RuntimeException("Comprador não encontrado!"));

        Order order = new Order();
        order.setCustomer(customer);
        order.setOrderDate(LocalDateTime.now());
        order.setStatus(OrderStatus.PROCESSING);
        order.setOrderNumber(UUID.randomUUID().toString());

        List<OrderItem> orderItems = new ArrayList<>();
        BigDecimal totalAmount = BigDecimal.ZERO;

        for (OrderItemRequestDTO itemDTO : orderDTO.getItems()) {
            ProductVariation variation = productVariationRepository.findById(itemDTO.getVariationId())
                    .orElseThrow(() -> new RuntimeException("Variação de produto não encontrada!"));

            if (variation.getStock() < itemDTO.getQuantity()) {
                throw new RuntimeException("Estoque insuficiente para a variação: " + variation.getName());
            }

            OrderItem orderItem = new OrderItem();
            orderItem.setProduct(variation.getProduct());
            orderItem.setQuantity(itemDTO.getQuantity());
            orderItem.setUnitPrice(variation.getPrice());
            orderItem.setOrder(order);
            orderItems.add(orderItem);

            variation.setStock(variation.getStock() - itemDTO.getQuantity());
            productVariationRepository.save(variation);

            totalAmount = totalAmount.add(variation.getPrice().multiply(new BigDecimal(itemDTO.getQuantity())));
        }

        order.setItems(orderItems);
        order.setTotalAmount(totalAmount);

        Order savedOrder = orderRepository.save(order);

        return new  OrderResponseDTO(savedOrder);
    }

    @Transactional
    public OrderResponseDTO createOrderFromCart(Long customerId) {

        User customer = userRepository.findById(customerId)
                .orElseThrow(() -> new RuntimeException("Usuário não encontrado!"));

        CarrinhoDeCompras cart = carrinhoRepository.findByUsuarioId(customer.getId())
                .orElseThrow(() -> new RuntimeException("Usuário não possui um carrinho de compras."));

        if (cart.getItens() == null || cart.getItens().isEmpty()) {
            throw new RuntimeException("Seu carrinho está vazio!");
        }

        Order order = new Order();
        order.setCustomer(customer);
        order.setOrderDate(LocalDateTime.now());
        order.setStatus(OrderStatus.PROCESSING);
        order.setOrderNumber(UUID.randomUUID().toString());

        List<OrderItem> orderItems = new ArrayList<>();
        BigDecimal totalAmount = BigDecimal.ZERO;

        for (CarrinhoDeComprasItem cartItems : cart.getItens()) {
            ProductVariation variation = cartItems.getVariacao();

            if (variation.getStock() < cartItems.getQuantidade()) {
                throw new RuntimeException("Estoque insuficiente para o produto: " + variation.getName());
            }

            OrderItem orderItem = new OrderItem();
            orderItem.setProduct(variation.getProduct());
            orderItem.setQuantity(cartItems.getQuantidade());
            orderItem.setUnitPrice(variation.getPrice());
            orderItem.setOrder(order);
            orderItems.add(orderItem);

            variation.setStock(variation.getStock() - cartItems.getQuantidade());
            productVariationRepository.save(variation);

            totalAmount = totalAmount.add(variation.getPrice().multiply(new BigDecimal(cartItems.getQuantidade())));
        }

        order.setItems(orderItems);
        order.setTotalAmount(totalAmount);

        Order savedOrder = orderRepository.save(order);

        cart.getItens().clear();
        carrinhoRepository.save(cart);

        return new OrderResponseDTO(savedOrder);
    }

    @Transactional
    public OrderResponseDTO addTrackingCode(Long orderId, String trackingCode, Long sellerId) {

        Order foundOrder = orderRepository.findById(orderId)
                .orElseThrow(() -> new RuntimeException("Pedido não encontrado!"));

        authorizeSellerForOrder(foundOrder, sellerId);

        foundOrder.setTrackingCode(trackingCode);

        Order savedOrder = orderRepository.save(foundOrder);

        return new OrderResponseDTO(savedOrder);
    }

    @Transactional
    public OrderResponseDTO updateOrderStatus(Long orderId, OrderStatus newStatus, Long sellerId) {

        Order foundOrder = orderRepository.findById(orderId)
                .orElseThrow(() -> new RuntimeException("Pedido não encontrado!"));

        authorizeSellerForOrder(foundOrder, sellerId);

        foundOrder.setStatus(newStatus);

        Order updatedOrder = orderRepository.save(foundOrder);

        return new OrderResponseDTO(updatedOrder);
    }

    private void authorizeSellerForOrder(Order order, Long sellerId) {

        // Verifica se pelo menos 1 item do pedido é do vendedor
        boolean isSellerOfAnItem = order.getItems().stream()
                .anyMatch(item -> item.getProduct().getId().equals(sellerId));

        if (!isSellerOfAnItem) {
            throw new RuntimeException("Acesso negado: Você não é o vendedor de nenhum item neste pedido.");
        }
    }


}