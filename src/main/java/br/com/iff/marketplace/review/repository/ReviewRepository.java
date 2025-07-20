package br.com.iff.marketplace.review.repository;

import br.com.iff.marketplace.review.Review;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import org.springframework.data.jpa.repository.Query;
import java.util.List;

@Repository
public interface ReviewRepository extends JpaRepository<Review, Long> {

    boolean existsByOrderId(Long orderId);

    List<Review> findByProductId(Long productId);

    boolean existsByCustomerIdAndProductId(Long customerId, Long productId);

}