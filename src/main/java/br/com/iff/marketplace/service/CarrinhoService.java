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
import br.com.iff.marketplace.controller.dto.UpdateItemCarrinhoDTO;

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

    @Transactional
    public CarrinhoDeCompras removerItem(Long itemId) {
        Usuario usuarioLogado = (Usuario) SecurityContextHolder.getContext().getAuthentication().getPrincipal();

        CarrinhoDeCompras carrinho = carrinhoRepository.findByUsuarioId(usuarioLogado.getId())
                .orElseThrow(() -> new RuntimeException("Usuário não possui um carrinho de compras."));

        boolean foiRemovido = carrinho.getItens().removeIf(item -> item.getId().equals(itemId));

        if (!foiRemovido) {
            throw new RuntimeException("Item não encontrado no carrinho!");
        }

        return carrinhoRepository.save(carrinho);
    }

    public CarrinhoDeCompras getMeuCarrinho() {
        Usuario usuarioLogado = (Usuario) SecurityContextHolder.getContext().getAuthentication().getPrincipal();

        return carrinhoRepository.findByUsuarioId(usuarioLogado.getId())
                .orElseGet(() -> {
                    CarrinhoDeCompras novoCarrinho = new CarrinhoDeCompras();
                    novoCarrinho.setUsuario(usuarioLogado);
                    return carrinhoRepository.save(novoCarrinho);
                });
    }

    @Transactional
    public CarrinhoDeCompras atualizarQuantidadeItem(Long itemId, UpdateItemCarrinhoDTO dto) {
        Usuario usuarioLogado = (Usuario) SecurityContextHolder.getContext().getAuthentication().getPrincipal();

        CarrinhoDeCompras carrinho = carrinhoRepository.findByUsuarioId(usuarioLogado.getId())
                .orElseThrow(() -> new RuntimeException("Usuário não possui um carrinho de compras."));

        CarrinhoDeComprasItem itemParaAtualizar = carrinho.getItens().stream()
                .filter(item -> item.getId().equals(itemId))
                .findFirst()
                .orElseThrow(() -> new RuntimeException("Item não encontrado no carrinho!"));

        if (dto.getQuantidade() <= 0) {
            throw new IllegalArgumentException("A quantidade deve ser maior que zero.");
        }

        if (itemParaAtualizar.getVariacao().getEstoque() < dto.getQuantidade()) {
            throw new RuntimeException("Estoque insuficiente. Disponível: " + itemParaAtualizar.getVariacao().getEstoque());
        }

        itemParaAtualizar.setQuantidade(dto.getQuantidade());

        return carrinhoRepository.save(carrinho);
    }

}