package br.com.iff.marketplace.service;

import br.com.iff.marketplace.controller.dto.ProdutoRequestDTO;
import br.com.iff.marketplace.model.Categoria;
import br.com.iff.marketplace.model.Produto;
import br.com.iff.marketplace.model.Usuario;
import br.com.iff.marketplace.repository.CategoriaRepository;
import br.com.iff.marketplace.repository.ProdutoRepository;
import br.com.iff.marketplace.repository.UsuarioRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.util.List;
import br.com.iff.marketplace.controller.dto.ProdutoResponseDTO;
import java.util.stream.Collectors;

@Service
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

    public List<ProdutoResponseDTO> listarProdutos() {
        List<Produto> produtos = produtoRepository.findAll();

        return produtos.stream()
                .map(ProdutoResponseDTO::new)
                .collect(Collectors.toList());
    }
}