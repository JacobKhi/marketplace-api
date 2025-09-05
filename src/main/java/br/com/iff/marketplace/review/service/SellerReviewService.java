package br.com.iff.marketplace.review.service;

import br.com.iff.marketplace.exception.NotFoundException;
import br.com.iff.marketplace.review.Review;
import br.com.iff.marketplace.review.dto.ReviewResponseDTO;
import br.com.iff.marketplace.review.repository.ReviewRepository;
import br.com.iff.marketplace.user.User;
import lombok.RequiredArgsConstructor;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;

@Service
@RequiredArgsConstructor
public class SellerReviewService {

    public final ReviewRepository reviewRepository;

    @Transactional
    public ReviewResponseDTO addSellerResponse(
            Long reviewId,
            String response,
            User seller) {

        Review foundReview = reviewRepository.findById(reviewId)
                .orElseThrow(() -> new NotFoundException("Avalicão de ID " + reviewId + " não encontrada"));

        if(!foundReview.getProduct().getSeller().getId().equals(seller.getId())) {
            throw new AccessDeniedException("Você só pode responder avaliações de seus próprios produtos");
        }

        foundReview.setSellerResponse(response);
        foundReview.setResponseDate(LocalDateTime.now());

        Review savedReview = reviewRepository.save(foundReview);
        return new ReviewResponseDTO(savedReview);
    }

    @Transactional
    public ReviewResponseDTO updateSellerResponse(
            Long reviewId,
            String newResponse,
            User seller) {

        Review foundReview = reviewRepository.findById(reviewId)
                .orElseThrow(() -> new NotFoundException("Avalicão de ID " + reviewId + " não encontrada"));

        if (!foundReview.getProduct().getSeller().getId().equals(seller.getId())) {
            throw new AccessDeniedException("Você não tem permissão para editar a resposta desta avaliação.");
        }

        foundReview.setSellerResponse(newResponse);
        foundReview.setResponseDate(LocalDateTime.now());

        Review savedReview = reviewRepository.save(foundReview);
        return new ReviewResponseDTO(savedReview);
    }

    @Transactional
    public ReviewResponseDTO deleteSellerResponse(
            Long reviewId,
            User seller) {

        Review foundReview = reviewRepository.findById(reviewId)
                .orElseThrow(() -> new NotFoundException("Avaliação de id " + reviewId + " não encontrada"));

        if (!foundReview.getProduct().getSeller().getId().equals(seller.getId())) {
            throw new AccessDeniedException("Você não tem permissão para modificar a resposta desta avaliação.");
        }

        foundReview.setSellerResponse(null);
        foundReview.setResponseDate(null);

        Review savedReview = reviewRepository.save(foundReview);
        return new ReviewResponseDTO(savedReview);
    }

}
