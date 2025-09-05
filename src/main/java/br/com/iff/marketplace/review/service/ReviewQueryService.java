package br.com.iff.marketplace.review.service;

import br.com.iff.marketplace.review.Review;
import br.com.iff.marketplace.review.dto.ReviewResponseDTO;
import br.com.iff.marketplace.review.repository.ReviewRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class ReviewQueryService {

    private final ReviewRepository reviewRepository;

    public Page<ReviewResponseDTO> listByProducts(
            Long ProductId,
            Pageable pageable) {

        Page<Review> reviewsPage = reviewRepository.findByProductId(ProductId, pageable);
        return reviewsPage.map(ReviewResponseDTO::new);
    }

}