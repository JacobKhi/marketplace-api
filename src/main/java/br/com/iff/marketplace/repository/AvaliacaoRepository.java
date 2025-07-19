package br.com.iff.marketplace.repository;

import br.com.iff.marketplace.model.Avaliacao;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import org.springframework.data.jpa.repository.Query;
import java.util.List;

@Repository
public interface AvaliacaoRepository extends JpaRepository<Avaliacao, Long> {

    // Este método irá gerar a consulta: "SELECT COUNT(*) FROM avaliacao WHERE pedido_id = ?"
    // e retornar true se o resultado for maior que zero.
    boolean existsByPedidoId(Long pedidoId);

    @Query("SELECT a FROM Avaliacao a JOIN a.pedido p JOIN p.items i WHERE i.product.id = :produtoId")
    List<Avaliacao> findAllByProdutoId(Long produtoId);
}