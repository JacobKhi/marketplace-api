package br.com.iff.marketplace.review.controller;

import br.com.iff.marketplace.review.dto.ReviewResponseDTO;
import br.com.iff.marketplace.review.service.ReviewQueryService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

@RestController
@RequestMapping("/api/v1")
@RequiredArgsConstructor
public class ReviewPublicController {

    private final ReviewQueryService publicReviewService;

    @GetMapping("/products/{productId}/reviews")
    public ResponseEntity<Page<ReviewResponseDTO>> listProductReviews(
            @PathVariable Long productId,
            Pageable pageable) {

        Page<ReviewResponseDTO> reviews = publicReviewService.listByProducts(productId, pageable);
        return ResponseEntity.ok(reviews);
    }

}
