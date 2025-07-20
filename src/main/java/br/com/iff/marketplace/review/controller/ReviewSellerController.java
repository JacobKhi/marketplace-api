package br.com.iff.marketplace.review.controller;

import br.com.iff.marketplace.review.Review;
import br.com.iff.marketplace.review.dto.ReviewResponseDTO;
import br.com.iff.marketplace.review.dto.SellerResponseRequestDTO;
import br.com.iff.marketplace.review.service.ReviewService;
import br.com.iff.marketplace.user.User;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RestController
@RequestMapping("/api/v1/seller/reviews")
@PreAuthorize("hasRole('SELLER')")
@RequiredArgsConstructor
public class ReviewSellerController {

    private final ReviewService reviewService;

    // Endpoint para o VENDEDOR responder a uma avaliação
    @PostMapping("/{reviewId}/response")
    public ResponseEntity<ReviewResponseDTO> addSellerResponse(
            @PathVariable Long reviewId,
            @RequestBody @Valid SellerResponseRequestDTO sellerResponseDTO,
            Authentication authentication) {

        User seller = (User) authentication.getPrincipal();
        ReviewResponseDTO updateReview = reviewService.addSellerResponse(reviewId, sellerResponseDTO.getResponse(), seller);

        return ResponseEntity.ok(updateReview);
    }

}
