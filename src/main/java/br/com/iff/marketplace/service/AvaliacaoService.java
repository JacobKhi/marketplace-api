package br.com.iff.marketplace.service;

import br.com.iff.marketplace.controller.dto.AvaliacaoRequestDTO;
import br.com.iff.marketplace.model.Avaliacao;
import br.com.iff.marketplace.model.Pedido;
import br.com.iff.marketplace.model.User;
import br.com.iff.marketplace.repository.AvaliacaoRepository;
import br.com.iff.marketplace.repository.PedidoRepository;
import br.com.iff.marketplace.repository.UsuarioRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import br.com.iff.marketplace.exception.NotFoundException;
import java.time.LocalDateTime;
import br.com.iff.marketplace.controller.dto.AvaliacaoResponseDTO;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class AvaliacaoService {

    private final AvaliacaoRepository avaliacaoRepository;

    private final PedidoRepository pedidoRepository;

    private final UsuarioRepository usuarioRepository;

    @Transactional
    public Avaliacao criarAvaliacao(AvaliacaoRequestDTO dto) {

        if (avaliacaoRepository.existsByPedidoId(dto.getPedidoId())) {
            throw new RuntimeException("Este pedido já foi avaliado!");
        }

        Pedido pedido = pedidoRepository.findById(dto.getPedidoId())
                .orElseThrow(() -> new NotFoundException("Pedido não encontrado!"));

        User avaliador = usuarioRepository.findById(dto.getAvaliadorId())
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

    @Transactional
    public Avaliacao adicionarResposta(Long avaliacaoId, String resposta) {

        User userLogado = (User) SecurityContextHolder.getContext().getAuthentication().getPrincipal();

        Avaliacao avaliacao = avaliacaoRepository.findById(avaliacaoId)
                .orElseThrow(() -> new RuntimeException("Avaliação não encontrada!"));

        Pedido pedidoDaAvaliacao = avaliacao.getPedido();

        User vendedorDoPedido = pedidoDaAvaliacao.getItens().getFirst().getProduto().getVendedor();

        if (!vendedorDoPedido.getId().equals(userLogado.getId())) {
            throw new RuntimeException("Acesso negado: Você só pode responder avaliações de seus próprios produtos.");
        }

        avaliacao.setRespostaVendedor(resposta);
        avaliacao.setDataResposta(LocalDateTime.now());

        return avaliacaoRepository.save(avaliacao);
    }

    public List<AvaliacaoResponseDTO> listarPorProduto(Long produtoId) {
        List<Avaliacao> avaliacoes = avaliacaoRepository.findAllByProdutoId(produtoId);

        return avaliacoes.stream()
                .map(AvaliacaoResponseDTO::new)
                .collect(Collectors.toList());
    }

}