package br.com.iff.marketplace.product;

import br.com.iff.marketplace.product.dto.ProductRequestDTO;
import br.com.iff.marketplace.controller.dto.VariacaoRequestDTO;
import br.com.iff.marketplace.category.Category;
import br.com.iff.marketplace.model.User;
import br.com.iff.marketplace.model.VariacaoProduto;
import br.com.iff.marketplace.category.CategoryRepository;
import br.com.iff.marketplace.repository.UsuarioRepository;
import br.com.iff.marketplace.repository.VariacaoProdutoRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import java.util.List;
import br.com.iff.marketplace.product.dto.ProductResponseDTO;
import java.util.stream.Collectors;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.data.jpa.domain.Specification;
import br.com.iff.marketplace.repository.specifications.ProdutoSpecification;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;

@Service
@Slf4j
@RequiredArgsConstructor
public class ProductService {

    private final ProductRepository productRepository;

    private final CategoryRepository categoryRepository;

    private final UsuarioRepository usuarioRepository;

    private final VariacaoProdutoRepository variacaoProdutoRepository;

    public Product salvarProduto(ProductRequestDTO dto) {
        Category categoria = categoryRepository.findById(dto.getCategoriaId())
                .orElseThrow(() -> new RuntimeException("Categoria não encontrada!"));

        User vendedor = usuarioRepository.findById(dto.getVendedorId())
                .orElseThrow(() -> new RuntimeException("Vendedor não encontrado!"));

        Product produto = new Product();
        produto.setName(dto.getNome());
        produto.setDescription(dto.getDescricao());

        produto.setCategory(categoria);
        produto.setSeller(vendedor);

        return productRepository.save(produto);
    }

    public Product atualizarProduto(Long id, ProductRequestDTO dto) {
        User userLogado = (User) SecurityContextHolder.getContext().getAuthentication().getPrincipal();

        Product produtoEncontrado = productRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Produto não encontrado!"));

        if (!produtoEncontrado.getSeller().getId().equals(userLogado.getId())) {
            throw new RuntimeException("Acesso negado: Você só pode editar seus próprios produtos.");
        }

        produtoEncontrado.setName(dto.getNome());
        produtoEncontrado.setDescription(dto.getDescricao());

        return productRepository.save(produtoEncontrado);
    }

    public void deletarProduto(Long id) {

        User userLogado = (User) SecurityContextHolder.getContext().getAuthentication().getPrincipal();

        Product produtoEncontrado = productRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Produto não encontrado!"));

        if (!produtoEncontrado.getSeller().getId().equals(userLogado.getId())) {
            throw new RuntimeException("Acesso negado: Você só pode deletar seus próprios produtos.");
        }

        productRepository.deleteById(id);
    }

    public Product buscarPorId(Long id) {
        return productRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Produto não encontrado!"));
    }

    public VariacaoProduto adicionarVariacao(Long produtoId, VariacaoRequestDTO variacaoDTO) {
        log.info("SERVICE: Tentando adicionar variação ao produto ID: {}", produtoId);

        User userLogado = (User) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        log.info("SERVICE: Usuário logado tem ID: {}", userLogado.getId());

        Product produtoPai = productRepository.findById(produtoId)
                .orElseThrow(() -> new RuntimeException("Produto não encontrado!"));
        log.info("SERVICE: Produto 'pai' encontrado. Dono do produto é o Vendedor ID: {}", produtoPai.getSeller().getId());


        if (!produtoPai.getSeller().getId().equals(userLogado.getId())) {
            log.warn("SERVICE: ACESSO NEGADO! Usuário {} não é dono do produto {}", userLogado.getId(), produtoId);
            throw new RuntimeException("Acesso negado: Você só pode adicionar variações aos seus próprios produtos.");
        }

        VariacaoProduto novaVariacao = new VariacaoProduto();
        novaVariacao.setNome(variacaoDTO.getNome());
        novaVariacao.setSku(variacaoDTO.getSku());
        novaVariacao.setPreco(variacaoDTO.getPreco());
        novaVariacao.setEstoque(variacaoDTO.getEstoque());

        novaVariacao.setProduto(produtoPai);

        return variacaoProdutoRepository.save(novaVariacao);
    }

    public List<ProductResponseDTO> listarProdutos(
            String nome, Long categoriaId, BigDecimal precoMin, BigDecimal precoMax) {

        Specification<Product> spec = (root, query, builder) -> builder.conjunction();

        if (nome != null && !nome.isBlank()) {
            spec = spec.and(ProdutoSpecification.comNome(nome));
        }
        if (categoriaId != null) {
            spec = spec.and(ProdutoSpecification.comCategoria(categoriaId));
        }
        if (precoMin != null || precoMax != null) {
            spec = spec.and(ProdutoSpecification.comPrecoEntre(precoMin, precoMax));
        }

        List<Product> produtos = productRepository.findAll(spec);

        return produtos.stream()
                .map(ProductResponseDTO::new)
                .collect(Collectors.toList());
    }

    @Transactional
    public VariacaoProduto atualizarVariacao(Long variacaoId, VariacaoRequestDTO variacaoDTO) {
        User userLogado = (User) SecurityContextHolder.getContext().getAuthentication().getPrincipal();

        VariacaoProduto variacao = variacaoProdutoRepository.findById(variacaoId)
                .orElseThrow(() -> new RuntimeException("Variação não encontrada!"));

        if (!variacao.getProduto().getSeller().getId().equals(userLogado.getId())) {
            throw new RuntimeException("Acesso negado: Você só pode editar as variações de seus próprios produtos.");
        }

        variacao.setNome(variacaoDTO.getNome());
        variacao.setSku(variacaoDTO.getSku());
        variacao.setPreco(variacaoDTO.getPreco());
        variacao.setEstoque(variacaoDTO.getEstoque());

        return variacaoProdutoRepository.save(variacao);
    }

    @Transactional
    public void deletarVariacao(Long variacaoId) {
        User userLogado = (User) SecurityContextHolder.getContext().getAuthentication().getPrincipal();

        VariacaoProduto variacao = variacaoProdutoRepository.findById(variacaoId)
                .orElseThrow(() -> new RuntimeException("Variação não encontrada!"));

        if (!variacao.getProduto().getSeller().getId().equals(userLogado.getId())) {
            throw new RuntimeException("Acesso negado: Você só pode deletar as variações de seus próprios produtos.");
        }

        variacaoProdutoRepository.delete(variacao);
    }

    public List<ProductResponseDTO> listarProdutosDoVendedorLogado() {
        User vendedorLogado = (User) SecurityContextHolder.getContext().getAuthentication().getPrincipal();

        List<Product> produtos = productRepository.findByVendedorId(vendedorLogado.getId());

        return produtos.stream()
                .map(ProductResponseDTO::new)
                .collect(Collectors.toList());
    }

}