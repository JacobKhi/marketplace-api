package br.com.iff.marketplace.service;

import br.com.iff.marketplace.model.Categoria;
import br.com.iff.marketplace.repository.CategoriaRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.util.List;

@Service
public class CategoriaService {

    @Autowired
    private CategoriaRepository repository;

    public Categoria salvarCategoria(Categoria categoria) {
        // Por enquanto, a lógica é simples: apenas salvar.
        // Futuramente, poderíamos adicionar validações aqui (ex: verificar se o nome já existe)
        return repository.save(categoria);
    }

    public List<Categoria> listarCategorias() {
        return repository.findAll();
    }
}