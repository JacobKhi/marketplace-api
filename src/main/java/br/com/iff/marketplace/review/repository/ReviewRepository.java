package br.com.iff.marketplace.review.repository;

import br.com.iff.marketplace.review.Review;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

@Repository
public interface ReviewRepository extends JpaRepository<Review, Long> {

    Page<Review> findByProductId(Long productId, Pageable pageable);

    Page<Review> findByCustomerId(Long customerId, Pageable pageable);

    boolean existsByCustomerIdAndProductId(Long customerId, Long productId);

}