package br.com.iff.marketplace.repository;

import br.com.iff.marketplace.model.Avaliacao;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface AvaliacaoRepository extends JpaRepository<Avaliacao, Long> {

    // Este método irá gerar a consulta: "SELECT COUNT(*) FROM avaliacao WHERE pedido_id = ?"
    // e retornar true se o resultado for maior que zero.
    boolean existsByPedidoId(Long pedidoId);
}