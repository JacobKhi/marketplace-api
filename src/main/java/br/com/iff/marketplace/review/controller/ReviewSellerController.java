package br.com.iff.marketplace.review.controller;

import br.com.iff.marketplace.review.dto.ReviewResponseDTO;
import br.com.iff.marketplace.review.dto.SellerResponseRequestDTO;
import br.com.iff.marketplace.review.service.ReviewService;
import br.com.iff.marketplace.user.User;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RestController
@RequestMapping("/api/v1/seller/reviews")
@PreAuthorize("hasRole('SELLER')")
@RequiredArgsConstructor
public class ReviewSellerController {

    private final ReviewService reviewService;

    @PostMapping("/{reviewId}/response")
    public ResponseEntity<ReviewResponseDTO> addSellerResponse(
            @PathVariable Long reviewId,
            @RequestBody @Valid SellerResponseRequestDTO sellerResponseDTO,
            @AuthenticationPrincipal User seller) {

        ReviewResponseDTO updateReview = reviewService.addSellerResponse(reviewId, sellerResponseDTO.getResponse(), seller);
        return ResponseEntity.ok(updateReview);
    }

    @PutMapping("/{reviewId}/response")
    public ResponseEntity<ReviewResponseDTO> updateSellerResponse(
            @PathVariable Long reviewId,
            @RequestBody @Valid SellerResponseRequestDTO sellerResponseDTO,
            @AuthenticationPrincipal User seller) {

        ReviewResponseDTO updatedReview = reviewService.updateSellerResponse(reviewId, sellerResponseDTO.getResponse(), seller);
        return ResponseEntity.ok(updatedReview);
    }

    @DeleteMapping("/{reviewId}/response")
    public ResponseEntity<ReviewResponseDTO> deleteSellerResponse(
            @PathVariable Long reviewId,
            @AuthenticationPrincipal User seller) {

        ReviewResponseDTO updatedReview = reviewService.deleteSellerResponse(reviewId, seller);
        return ResponseEntity.ok(updatedReview);
    }

}
