package br.com.iff.marketplace.order.repository;

import br.com.iff.marketplace.order.Order;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

@Repository
public interface OrderRepository extends JpaRepository<Order, Long> {

    Page<Order> findByCustomerId(Long custommerId, Pageable pageable);

    @Query("SELECT DISTINCT p FROM Order p JOIN p.items i JOIN i.product prod WHERE prod.seller.id = :sellerId")
    Page<Order> findBySellerId(Long sellerId, Pageable pageable);

}