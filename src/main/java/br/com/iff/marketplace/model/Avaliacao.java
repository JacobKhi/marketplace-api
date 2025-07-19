package br.com.iff.marketplace.model;

import jakarta.persistence.*;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.time.LocalDateTime;

@Data
@EqualsAndHashCode(of = "id")
@Entity
public class Avaliacao {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private Integer nota; // Ex -> de 1 a 5

    @Column(length = 1000)
    private String comentario;

    private LocalDateTime dataAvaliacao;

    @Column(length = 1000)
    private String respostaVendedor;

    private LocalDateTime dataResposta;

    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "pedido_id", unique = true)
    private Order pedido;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "avaliador_id")
    private User avaliador;

}