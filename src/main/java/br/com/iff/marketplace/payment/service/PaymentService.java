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
import com.stripe.model.Charge;
import com.stripe.param.ChargeCreateParams;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;

@Service
@RequiredArgsConstructor
public class PaymentService {

    private final OrderRepository orderRepository;

    @Value("${stripe.secret.key}")
    private String stripeSecretKey;

    @Transactional
    public PaymentResponseDTO processPayment(PaymentRequestDTO paymentRequest, User customer) {

        Order order = orderRepository.findById(paymentRequest.getOrderId())
                .orElseThrow(() -> new NotFoundException("Pedido não encontrado!"));

        if (!order.getCustomer().getId().equals(customer.getId())) {
            throw new AccessDeniedException("Você só pode pagar pelos seus próprios pedidos.");
        }

        if (order.getStatus() != OrderStatus.PROCESSING) {
            throw new IllegalStateException("Este pedido não pode ser pago, pois seu status atual é: " + order.getStatus());
        }

        try {
            Stripe.apiKey = stripeSecretKey;

            ChargeCreateParams params = ChargeCreateParams.builder()
                    .setAmount(order.getTotalAmount().multiply(new BigDecimal("100")).longValue())
                    .setCurrency("brl")
                    .setDescription("Pagamento do Pedido #" + order.getOrderNumber())
                    .setSource(paymentRequest.getPaymentToken())
                    .build();

            Charge charge = Charge.create(params);

            if ("succeeded".equals(charge.getStatus())) {
                order.setStatus(OrderStatus.PAYMENT_APPROVED);
                orderRepository.save(order);
            } else {
                throw new RuntimeException("Falha no pagamento: " + charge.getFailureMessage());
            }

            return new PaymentResponseDTO(charge.getId(), charge.getStatus(), order.getOrderNumber());

        } catch (StripeException e) {
            throw new RuntimeException("Erro ao processar pagamento com Stripe: " + e.getMessage());
        }
    }


}