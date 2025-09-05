package br.com.iff.marketplace.order.service;

import br.com.iff.marketplace.cart.ShoppingCart;
import br.com.iff.marketplace.cart.ShoppingCartItem;
import br.com.iff.marketplace.cart.repository.ShoppingCartRepository;
import br.com.iff.marketplace.exception.NotFoundException;
import br.com.iff.marketplace.order.Order;
import br.com.iff.marketplace.order.OrderItem;
import br.com.iff.marketplace.order.dto.OrderItemRequestDTO;
import br.com.iff.marketplace.order.dto.OrderRequestDTO;
import br.com.iff.marketplace.order.dto.OrderResponseDTO;
import br.com.iff.marketplace.order.enums.OrderStatus;
import br.com.iff.marketplace.order.repository.OrderRepository;
import br.com.iff.marketplace.product.ProductVariation;
import br.com.iff.marketplace.product.repository.ProductVariationRepository;
import br.com.iff.marketplace.user.User;
import br.com.iff.marketplace.user.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class CustomerOrderService {

    private final OrderRepository orderRepository;
    private final UserRepository userRepository;
    private final ProductVariationRepository productVariationRepository;
    private final ShoppingCartRepository shoppingCartRepository;

    @Transactional
    public OrderResponseDTO createDirectOrder(
            OrderRequestDTO orderDTO,
            Long customerId) {

        User customer = userRepository.findById(customerId)
                .orElseThrow(() -> new NotFoundException("Comprador com ID " + customerId + " não encontrado!"));

        Order order = new Order();
        order.setCustomer(customer);
        order.setOrderDate(LocalDateTime.now());
        order.setStatus(OrderStatus.PROCESSING);
        order.setOrderNumber(UUID.randomUUID().toString());

        List<OrderItem> orderItems = new ArrayList<>();
        BigDecimal totalAmount = BigDecimal.ZERO;

        for (OrderItemRequestDTO itemDTO : orderDTO.getItems()) {
            ProductVariation variation = productVariationRepository.findById(itemDTO.getVariationId())
                    .orElseThrow(() -> new NotFoundException("Variação de ID " + itemDTO.getVariationId() + " produto não encontrada!"));

            if (variation.getStock() < itemDTO.getQuantity()) {
                throw new IllegalStateException("Estoque insuficiente para a variação: " + variation.getName());
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
        return new OrderResponseDTO(savedOrder);
    }

    @Transactional
    public OrderResponseDTO createOrderFromCart(Long customerId) {

        User customer = userRepository.findById(customerId)
                .orElseThrow(() -> new NotFoundException("Comprador com ID " + customerId + " não encontrado!"));

        ShoppingCart cart = shoppingCartRepository.findByUserId(customer.getId())
                .orElseThrow(() -> new NotFoundException("Comprador de ID " + customerId + " não não possui um carrinho!"));

        if (cart.getItems() == null || cart.getItems().isEmpty()) {
            throw new IllegalStateException("Seu carrinho está vazio!");
        }

        Order order = new Order();
        order.setCustomer(customer);
        order.setOrderDate(LocalDateTime.now());
        order.setStatus(OrderStatus.PROCESSING);
        order.setOrderNumber(UUID.randomUUID().toString());

        List<OrderItem> orderItems = new ArrayList<>();
        BigDecimal totalAmount = BigDecimal.ZERO;

        for (ShoppingCartItem cartItems : cart.getItems()) {
            ProductVariation variation = cartItems.getVariation();

            if (variation.getStock() < cartItems.getQuantity()) {
                throw new IllegalStateException("Estoque insuficiente para o produto: " + variation.getName());
            }

            OrderItem orderItem = new OrderItem();
            orderItem.setProduct(variation.getProduct());
            orderItem.setQuantity(cartItems.getQuantity());
            orderItem.setUnitPrice(variation.getPrice());
            orderItem.setOrder(order);
            orderItems.add(orderItem);

            variation.setStock(variation.getStock() - cartItems.getQuantity());
            productVariationRepository.save(variation);

            totalAmount = totalAmount.add(variation.getPrice().multiply(new BigDecimal(cartItems.getQuantity())));
        }

        order.setItems(orderItems);
        order.setTotalAmount(totalAmount);

        Order savedOrder = orderRepository.save(order);
        cart.getItems().clear();
        shoppingCartRepository.save(cart);

        return new OrderResponseDTO(savedOrder);
    }

}