package br.com.iff.marketplace.payment.controller;

import br.com.iff.marketplace.order.repository.OrderRepository;
import br.com.iff.marketplace.payment.dto.PaymentRequestDTO;
import br.com.iff.marketplace.payment.dto.PaymentResponseDTO;
import br.com.iff.marketplace.payment.service.PaymentService;
import br.com.iff.marketplace.user.User;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/v1/payments")
@PreAuthorize("hasRole('CUSTOMER')")
@RequiredArgsConstructor
public class PagamentoController {

    private final PaymentService paymentService;
    private final OrderRepository orderRepository;

    @PostMapping
    public ResponseEntity<PaymentResponseDTO> processPayment(
            @RequestBody @Valid PaymentRequestDTO paymentDTO,
            Authentication authentication) {

        User customer = (User) authentication.getPrincipal();

        PaymentResponseDTO paymentResponse = paymentService.processPayment(paymentDTO, customer);

        return ResponseEntity.ok(paymentResponse);
    }

}