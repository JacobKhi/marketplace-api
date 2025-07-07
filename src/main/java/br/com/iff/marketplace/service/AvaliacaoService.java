package br.com.iff.marketplace.service;

import br.com.iff.marketplace.controller.dto.AvaliacaoRequestDTO;
import br.com.iff.marketplace.model.Avaliacao;
import br.com.iff.marketplace.model.Pedido;
import br.com.iff.marketplace.model.Usuario;
import br.com.iff.marketplace.repository.AvaliacaoRepository;
import br.com.iff.marketplace.repository.PedidoRepository;
import br.com.iff.marketplace.repository.UsuarioRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import br.com.iff.marketplace.exception.NotFoundException;

import java.time.LocalDateTime;

@Service
public class AvaliacaoService {

    @Autowired
    private AvaliacaoRepository avaliacaoRepository;

    @Autowired
    private PedidoRepository pedidoRepository;

    @Autowired
    private UsuarioRepository usuarioRepository;

    @Transactional
    public Avaliacao criarAvaliacao(AvaliacaoRequestDTO dto) {

        if (avaliacaoRepository.existsByPedidoId(dto.getPedidoId())) {
            throw new RuntimeException("Este pedido já foi avaliado!");
        }

        Pedido pedido = pedidoRepository.findById(dto.getPedidoId())
                .orElseThrow(() -> new NotFoundException("Pedido não encontrado!"));

        Usuario avaliador = usuarioRepository.findById(dto.getAvaliadorId())
                .orElseThrow(() -> new NotFoundException("Usuário não encontrado!"));

        if (!pedido.getComprador().getId().equals(avaliador.getId())) {
            throw new RuntimeException("Apenas o comprador pode avaliar o pedido.");
        }

        Avaliacao avaliacao = new Avaliacao();
        avaliacao.setNota(dto.getNota());
        avaliacao.setComentario(dto.getComentario());
        avaliacao.setDataAvaliacao(LocalDateTime.now());
        avaliacao.setPedido(pedido);
        avaliacao.setAvaliador(avaliador);

        return avaliacaoRepository.save(avaliacao);
    }
}