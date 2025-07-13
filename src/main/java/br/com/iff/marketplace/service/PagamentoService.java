package br.com.iff.marketplace.service;

import com.stripe.Stripe;
import jakarta.annotation.PostConstruct;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import com.stripe.exception.StripeException;
import com.stripe.model.Charge;
import com.stripe.param.ChargeCreateParams;
import java.math.BigDecimal;

@Service
public class PagamentoService {

    @Value("${stripe.api.secret-key}")
    private String secretKey;

    @PostConstruct
    public void init() {
        Stripe.apiKey = this.secretKey;
    }

    public Charge criarCobranca(String tokenPagamento, BigDecimal valor, Long pedidoId) throws StripeException {
        Long valorEmCentavos = valor.multiply(new BigDecimal("100")).longValue();

        ChargeCreateParams params =
                ChargeCreateParams.builder()
                        .setAmount(valorEmCentavos)
                        .setCurrency("brl")
                        .setSource(tokenPagamento)
                        .setDescription("Cobrança do Pedido #" + pedidoId + " no Marketplace")
                        .build();

        try {
            return Charge.create(params);
        } catch (StripeException e) {
            // log.error("Erro ao processar pagamento com Stripe: {}", e.getMessage());
            throw e;
        }
    }

}