package br.com.iff.marketplace.review.controller;

import br.com.iff.marketplace.review.Review;
import br.com.iff.marketplace.review.dto.ReviewRequestDTO;
import br.com.iff.marketplace.review.dto.ReviewResponseDTO;
import br.com.iff.marketplace.review.service.ReviewService;
import br.com.iff.marketplace.user.User;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/v1/reviews")
@PreAuthorize("hasRole('CUSTOMER')")
@RequiredArgsConstructor
public class ReviewCustomerController {

    private final ReviewService reviewService;

    // Endpoint para criar uma avaliacao
    @PostMapping
    public ResponseEntity<ReviewResponseDTO> createReview(
            @RequestBody @Valid ReviewRequestDTO reviewDTO,
            Authentication authentication) {

        User customer = (User) authentication.getPrincipal();
        ReviewResponseDTO novaReview = reviewService.createReview(reviewDTO, customer);

        return ResponseEntity.status(HttpStatus.CREATED).body(novaReview);
    }

}
