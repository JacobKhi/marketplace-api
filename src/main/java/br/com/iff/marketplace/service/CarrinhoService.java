package br.com.iff.marketplace.service;

import br.com.iff.marketplace.repository.CarrinhoDeComprasRepository;
import br.com.iff.marketplace.repository.VariacaoProdutoRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import br.com.iff.marketplace.controller.dto.AddItemCarrinhoDTO;
import br.com.iff.marketplace.model.CarrinhoDeCompras;
import br.com.iff.marketplace.model.CarrinhoDeComprasItem;
import br.com.iff.marketplace.model.Usuario;
import br.com.iff.marketplace.model.VariacaoProduto;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.transaction.annotation.Transactional;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class CarrinhoService {

    private final CarrinhoDeComprasRepository carrinhoRepository;
    private final VariacaoProdutoRepository variacaoProdutoRepository;

    @Transactional
    public CarrinhoDeCompras adicionarItem(AddItemCarrinhoDTO dto) {
        Usuario usuarioLogado = (Usuario) SecurityContextHolder.getContext().getAuthentication().getPrincipal();

        VariacaoProduto variacao = variacaoProdutoRepository.findById(dto.getVariacaoId())
                .orElseThrow(() -> new RuntimeException("Variação de produto não encontrada!"));

        CarrinhoDeCompras carrinho = carrinhoRepository.findByUsuarioId(usuarioLogado.getId())
                .orElseGet(() -> {
                    CarrinhoDeCompras novoCarrinho = new CarrinhoDeCompras();
                    novoCarrinho.setUsuario(usuarioLogado);
                    return novoCarrinho;
                });

        Optional<CarrinhoDeComprasItem> itemExistenteOpt = carrinho.getItens().stream()
                .filter(item -> item.getVariacao().getId().equals(dto.getVariacaoId()))
                .findFirst();

        if (itemExistenteOpt.isPresent()) {
            CarrinhoDeComprasItem itemExistente = itemExistenteOpt.get();
            itemExistente.setQuantidade(itemExistente.getQuantidade() + dto.getQuantidade());
        } else {
            CarrinhoDeComprasItem novoItem = new CarrinhoDeComprasItem();
            novoItem.setCarrinho(carrinho);
            novoItem.setVariacao(variacao);
            novoItem.setQuantidade(dto.getQuantidade());
            carrinho.getItens().add(novoItem);
        }

        return carrinhoRepository.save(carrinho);
    }


}