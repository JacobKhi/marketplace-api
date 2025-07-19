package br.com.iff.marketplace.repository;

import br.com.iff.marketplace.model.Order;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import org.springframework.data.jpa.repository.Query;
import java.util.List;

@Repository
public interface PedidoRepository extends JpaRepository<Order, Long> {

    List<Order> findByCompradorId(Long compradorId);

    @Query("SELECT DISTINCT p FROM Order p JOIN p.itens i JOIN i.produto prod WHERE prod.seller.id = :vendedorId")
    List<Order> findByVendedorId(Long vendedorId);
}