package br.com.iff.marketplace.payment.controller;

import br.com.iff.marketplace.payment.service.PaymentService;
import br.com.iff.marketplace.user.User;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RestController
@RequestMapping("/api/v1/payments")
@PreAuthorize("hasRole('CUSTOMER')")
@RequiredArgsConstructor
public class PaymentController {

    private final PaymentService paymentService;

    @PostMapping("/create-payment-intent/{orderId}")
    public ResponseEntity<Map<String, String>> createPaymentIntent(
            @PathVariable Long orderId,
            @AuthenticationPrincipal User customer) {

        Map<String, String> paymentIntentInfo = paymentService.createPaymentIntent(orderId, customer);
        return ResponseEntity.ok(paymentIntentInfo);
    }

    @PostMapping("/confirm-order/{orderId}")
    public ResponseEntity<Void> confirmOrder(
            @PathVariable Long orderId,
            @AuthenticationPrincipal User customer) {

        paymentService.confirmOrderPayment(orderId, customer);
        return ResponseEntity.ok().build();
    }

}