package br.com.iff.marketplace.service;

import br.com.iff.marketplace.controller.dto.ProdutoRequestDTO;
import br.com.iff.marketplace.model.Categoria;
import br.com.iff.marketplace.model.Produto;
import br.com.iff.marketplace.model.Usuario;
import br.com.iff.marketplace.repository.CategoriaRepository;
import br.com.iff.marketplace.repository.ProdutoRepository;
import br.com.iff.marketplace.repository.UsuarioRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.util.List;
import br.com.iff.marketplace.controller.dto.ProdutoResponseDTO;
import java.util.stream.Collectors;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.data.jpa.domain.Specification;
import br.com.iff.marketplace.repository.specifications.ProdutoSpecification;
import java.math.BigDecimal;

@Service
@Slf4j
public class ProdutoService {

    @Autowired
    private ProdutoRepository produtoRepository;

    @Autowired
    private CategoriaRepository categoriaRepository;

    @Autowired
    private UsuarioRepository usuarioRepository;

    public Produto salvarProduto(ProdutoRequestDTO dto) {
        Categoria categoria = categoriaRepository.findById(dto.getCategoriaId())
                .orElseThrow(() -> new RuntimeException("Categoria não encontrada!"));

        Usuario vendedor = usuarioRepository.findById(dto.getVendedorId())
                .orElseThrow(() -> new RuntimeException("Vendedor não encontrado!"));

        Produto produto = new Produto();
        produto.setNome(dto.getNome());
        produto.setDescricao(dto.getDescricao());
        produto.setPreco(dto.getPreco());
        produto.setEstoque(dto.getEstoque());

        produto.setCategoria(categoria);
        produto.setVendedor(vendedor);

        return produtoRepository.save(produto);
    }

    public Produto atualizarProduto(Long id, ProdutoRequestDTO dto) {
        log.debug("SERVICE: Iniciando atualização do produto ID: {}", id);

        Usuario usuarioLogado = (Usuario) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        log.debug("SERVICE: Usuário logado encontrado: ID {}", usuarioLogado.getId());

        Produto produtoEncontrado = produtoRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Produto não encontrado!"));
        log.debug("SERVICE: Produto encontrado no banco: ID do Vendedor é {}", produtoEncontrado.getVendedor().getId());

        if (!produtoEncontrado.getVendedor().getId().equals(usuarioLogado.getId())) {
            log.warn("SERVICE: ACESSO NEGADO! Usuário {} tentou editar produto do vendedor {}", usuarioLogado.getId(), produtoEncontrado.getVendedor().getId());
            throw new RuntimeException("Acesso negado: Você só pode editar seus próprios produtos.");
        }

        log.debug("SERVICE: Acesso permitido. Atualizando dados.");
        produtoEncontrado.setNome(dto.getNome());
        produtoEncontrado.setDescricao(dto.getDescricao());
        produtoEncontrado.setPreco(dto.getPreco());
        produtoEncontrado.setEstoque(dto.getEstoque());

        return produtoRepository.save(produtoEncontrado);
    }

    public void deletarProduto(Long id) {
        log.debug("SERVICE: Recebida requisição para deletar o produto ID: {}", id);

        Usuario usuarioLogado = (Usuario) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        log.debug("SERVICE: Usuário logado: ID {}", usuarioLogado.getId());

        Produto produtoEncontrado = produtoRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Produto não encontrado!"));
        log.debug("SERVICE: Produto encontrado. Vendedor do produto: ID {}", produtoEncontrado.getVendedor().getId());

        if (!produtoEncontrado.getVendedor().getId().equals(usuarioLogado.getId())) {
            log.warn("SERVICE: ACESSO NEGADO! Usuário {} tentou deletar produto do vendedor {}", usuarioLogado.getId(), produtoEncontrado.getVendedor().getId());
            throw new RuntimeException("Acesso negado: Você só pode deletar seus próprios produtos.");
        }

        produtoRepository.deleteById(id);
        log.debug("SERVICE: Produto ID {} deletado com sucesso pelo dono.", id);
    }

    public List<ProdutoResponseDTO> listarProdutos(
            String nome, Long categoriaId, BigDecimal precoMin, BigDecimal precoMax) {

        Specification<Produto> spec = (root, query, builder) -> builder.conjunction();

        if (nome != null && !nome.isBlank()) {
            spec = spec.and(ProdutoSpecification.comNome(nome));
        }
        if (categoriaId != null) {
            spec = spec.and(ProdutoSpecification.comCategoria(categoriaId));
        }
        if (precoMin != null || precoMax != null) {
            spec = spec.and(ProdutoSpecification.comPrecoEntre(precoMin, precoMax));
        }

        List<Produto> produtos = produtoRepository.findAll(spec);

        return produtos.stream()
                .map(ProdutoResponseDTO::new)
                .collect(Collectors.toList());
    }
}