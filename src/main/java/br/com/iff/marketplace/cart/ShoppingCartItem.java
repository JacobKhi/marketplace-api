package br.com.iff.marketplace.cart;

import br.com.iff.marketplace.product.ProductVariation;
import jakarta.persistence.*;
import lombok.Data;
import lombok.EqualsAndHashCode;

@Data
@EqualsAndHashCode(of = "id")
@Entity
public class ShoppingCartItem {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private Integer quantity;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "variacao_id")
    private ProductVariation variation;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "carrinho_id")
    private ShoppingCart cart;

}