package br.com.iff.marketplace.controller.dto;

import br.com.iff.marketplace.model.Avaliacao;
import lombok.Data;
import java.time.LocalDateTime;

@Data
public class AvaliacaoResponseDTO {

    private Long id;
    private Integer nota;
    private String comentario;
    private LocalDateTime dataAvaliacao;
    private String nomeAvaliador;
    private String numeroDoPedido;
    private String respostaVendedor;
    private LocalDateTime dataResposta;

    public AvaliacaoResponseDTO(Avaliacao avaliacao) {
        this.id = avaliacao.getId();
        this.nota = avaliacao.getNota();
        this.comentario = avaliacao.getComentario();
        this.dataAvaliacao = avaliacao.getDataAvaliacao();
        this.nomeAvaliador = avaliacao.getAvaliador().getName();
        this.numeroDoPedido = avaliacao.getPedido().getOrderNumber();
        this.respostaVendedor = avaliacao.getRespostaVendedor();
        this.dataResposta = avaliacao.getDataResposta();
    }
}