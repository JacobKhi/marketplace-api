package br.com.iff.marketplace.review.controller;

import br.com.iff.marketplace.review.dto.ReviewRequestDTO;
import br.com.iff.marketplace.review.dto.ReviewResponseDTO;
import br.com.iff.marketplace.review.dto.UpdateReviewDTO;
import br.com.iff.marketplace.review.service.ReviewService;
import br.com.iff.marketplace.user.User;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/reviews")
@PreAuthorize("hasRole('CUSTOMER')")
@RequiredArgsConstructor
public class ReviewCustomerController {

    private final ReviewService reviewService;

    @PostMapping
    public ResponseEntity<ReviewResponseDTO> createReview(
            @RequestBody @Valid ReviewRequestDTO reviewDTO,
            @AuthenticationPrincipal User customer) {

        ReviewResponseDTO novaReview = reviewService.createReview(reviewDTO, customer);
        return ResponseEntity.status(HttpStatus.CREATED).body(novaReview);
    }

    @PutMapping("/{reviewId}")
    public ResponseEntity<ReviewResponseDTO> updateReview(
            @PathVariable Long reviewId,
            @RequestBody @Valid UpdateReviewDTO reviewDTO,
            @AuthenticationPrincipal User customer) {

        ReviewResponseDTO updatedReview = reviewService.updateReview(reviewId, reviewDTO, customer);
        return ResponseEntity.ok(updatedReview);
    }

    @DeleteMapping("/{reviewId}")
    public ResponseEntity<Void> deleteReview(
            @PathVariable Long reviewId,
            @AuthenticationPrincipal User customer) {

        reviewService.deleteReview(reviewId, customer);
        return ResponseEntity.noContent().build();
    }

}
