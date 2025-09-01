package br.com.iff.marketplace.payment.service;

import br.com.iff.marketplace.exception.NotFoundException;
import br.com.iff.marketplace.order.Order;
import br.com.iff.marketplace.order.enums.OrderStatus;
import br.com.iff.marketplace.order.repository.OrderRepository;
import br.com.iff.marketplace.payment.dto.PaymentRequestDTO;
import br.com.iff.marketplace.payment.dto.PaymentResponseDTO;
import br.com.iff.marketplace.user.User;
import com.stripe.Stripe;
import com.stripe.exception.StripeException;
import com.stripe.model.PaymentIntent;
import com.stripe.param.PaymentIntentCreateParams;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.math.BigDecimal;
import java.util.HashMap;
import java.util.Map;

@Service
@RequiredArgsConstructor
public class PaymentService {

    private final OrderRepository orderRepository;

    @Value("${stripe.secret.key}")
    private String stripeSecretKey;

    @Transactional
    public Map<String, String> createPaymentIntent(Long orderId, User customer) {
        Order order = orderRepository.findById(orderId)
                .orElseThrow(() -> new NotFoundException("Pedido não encontrado!"));

        if (!order.getCustomer().getId().equals(customer.getId())) {
            throw new AccessDeniedException("Você só pode pagar pelos seus próprios pedidos.");
        }

        if (order.getStatus() != OrderStatus.PROCESSING) {
            throw new IllegalStateException("Este pedido não pode ser pago, pois seu status atual é: " + order.getStatus());
        }

        try {
            Stripe.apiKey = stripeSecretKey;

            PaymentIntentCreateParams params = PaymentIntentCreateParams.builder()
                    .setAmount(order.getTotalAmount().multiply(new BigDecimal("100")).longValue())
                    .setCurrency("brl")
                    .putMetadata("order_id", order.getId().toString())
                    .setAutomaticPaymentMethods(
                            PaymentIntentCreateParams.AutomaticPaymentMethods.builder().setEnabled(true).build()
                    )
                    .build();

            PaymentIntent paymentIntent = PaymentIntent.create(params);

            Map<String, String> response = new HashMap<>();
            response.put("clientSecret", paymentIntent.getClientSecret());
            return response;

        } catch (StripeException e) {
            throw new RuntimeException("Erro ao criar PaymentIntent com Stripe: " + e.getMessage());
        }
    }

    @Transactional
    public void confirmOrderPayment(Long orderId, User customer) {
        Order order = orderRepository.findById(orderId)
                .orElseThrow(() -> new NotFoundException("Pedido não encontrado!"));

        if (!order.getCustomer().getId().equals(customer.getId())) {
            throw new AccessDeniedException("Você não tem permissão para confirmar este pedido.");
        }

        if (order.getStatus() == OrderStatus.PROCESSING) {
            order.setStatus(OrderStatus.PAYMENT_APPROVED);
            orderRepository.save(order);
        }
    }

}