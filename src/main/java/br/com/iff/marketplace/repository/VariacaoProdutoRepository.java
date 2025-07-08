package br.com.iff.marketplace.repository;

import br.com.iff.marketplace.model.VariacaoProduto;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface VariacaoProdutoRepository extends JpaRepository<VariacaoProduto, Long> {
}