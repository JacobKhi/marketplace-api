package br.com.iff.marketplace.order.repository;

import br.com.iff.marketplace.order.Order;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import org.springframework.data.jpa.repository.Query;
import java.util.List;

@Repository
public interface OrderRepository extends JpaRepository<Order, Long> {

    List<Order> findByCompradorId(Long compradorId);

    @Query("SELECT DISTINCT p FROM Order p JOIN p.items i JOIN i.product prod WHERE prod.seller.id = :vendedorId")
    List<Order> findByVendedorId(Long vendedorId);
}