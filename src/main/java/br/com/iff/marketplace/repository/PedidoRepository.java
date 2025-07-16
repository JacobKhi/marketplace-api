package br.com.iff.marketplace.repository;

import br.com.iff.marketplace.model.Pedido;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import org.springframework.data.jpa.repository.Query;
import java.util.List;

@Repository
public interface PedidoRepository extends JpaRepository<Pedido, Long> {

    List<Pedido> findByCompradorId(Long compradorId);

    @Query("SELECT DISTINCT p FROM Pedido p JOIN p.itens i JOIN i.produto prod WHERE prod.vendedor.id = :vendedorId")
    List<Pedido> findByVendedorId(Long vendedorId);
}