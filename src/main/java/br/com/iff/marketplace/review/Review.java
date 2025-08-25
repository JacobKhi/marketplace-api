package br.com.iff.marketplace.review;

import br.com.iff.marketplace.order.Order;
import br.com.iff.marketplace.product.Product;
import br.com.iff.marketplace.user.User;
import jakarta.persistence.*;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.time.LocalDateTime;

@Data
@EqualsAndHashCode(of = "id")
@Entity
@Table(name = "reviews")
public class Review {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private Integer rating; // Ex -> de 1 a 5

    @Column(length = 1000)
    private String comment;

    private LocalDateTime reviewDate;

    @Column(length = 1000)
    private String sellerResponse;

    private LocalDateTime responseDate;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "product_id")
    private Product product;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "order_id")
    private Order order;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "customer_id")
    private User customer;

}