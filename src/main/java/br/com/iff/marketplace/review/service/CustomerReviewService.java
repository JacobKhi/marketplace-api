package br.com.iff.marketplace.review.service;

import br.com.iff.marketplace.exception.NotFoundException;
import br.com.iff.marketplace.order.Order;
import br.com.iff.marketplace.order.repository.OrderRepository;
import br.com.iff.marketplace.product.Product;
import br.com.iff.marketplace.product.repository.ProductRepository;
import br.com.iff.marketplace.review.Review;
import br.com.iff.marketplace.review.dto.ReviewRequestDTO;
import br.com.iff.marketplace.review.dto.ReviewResponseDTO;
import br.com.iff.marketplace.review.dto.UpdateReviewDTO;
import br.com.iff.marketplace.review.repository.ReviewRepository;
import br.com.iff.marketplace.user.User;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;

@Service
@RequiredArgsConstructor
public class CustomerReviewService {

    private final ReviewRepository reviewRepository;
    private final OrderRepository orderRepository;
    private final ProductRepository productRepository;

    public Page<ReviewResponseDTO> findMyReviews(
            User customer,
            Pageable pageable) {

        Page<Review> reviewsPage = reviewRepository.findByCustomerId(customer.getId(), pageable);
        return reviewsPage.map(ReviewResponseDTO::new);
    }

    @Transactional
    public ReviewResponseDTO createReview(
            ReviewRequestDTO reviewDTO,
            User customer) {

        Product foundProduct = productRepository.findById(reviewDTO.getProductId())
                .orElseThrow(() -> new NotFoundException("Produto de ID " + reviewDTO.getProductId() + " não encontrado"));

        Order foundOrder = orderRepository.findById(reviewDTO.getOrderId())
                .orElseThrow(() -> new NotFoundException("Pedido de ID " + reviewDTO.getOrderId() + " não encontrado"));

        if (!foundOrder.getCustomer().getId().equals(customer.getId())) {
            throw new AccessDeniedException("Você só pode avaliar pedidos que você fez.");
        }

        if (reviewRepository.existsByCustomerIdAndProductId(customer.getId(), foundProduct.getId())) {
            throw new IllegalStateException("Voce já avaliou esse produto");
        }

        boolean productInOrder = foundOrder.getItems().stream()
                .anyMatch(item -> item.getProduct().getId().equals(foundProduct.getId()));

        if (!productInOrder) {
            throw new IllegalStateException("O produto informado não faz parte do pedido especificado.");
        }

        Review newReview = new Review();
        newReview.setRating(reviewDTO.getRating());
        newReview.setComment(reviewDTO.getComment());
        newReview.setReviewDate(LocalDateTime.now());
        newReview.setProduct(foundProduct);
        newReview.setCustomer(customer);
        newReview.setOrder(foundOrder);

        Review savedReview = reviewRepository.save(newReview);

        return new ReviewResponseDTO(savedReview);
    }

    @Transactional
    public ReviewResponseDTO updateReview(
            Long reviewId,
            UpdateReviewDTO reviewDTO,
            User customer) {

        Review foundReview = reviewRepository.findById(reviewId)
                .orElseThrow(() -> new NotFoundException("Avaliação não encontrada"));

        if (!foundReview.getCustomer().getId().equals(customer.getId())) {
            throw new AccessDeniedException("Você não tem permissão para editar esta avaliação.");
        }

        foundReview.setRating(reviewDTO.getRating());
        foundReview.setComment(reviewDTO.getComment());
        foundReview.setReviewDate(LocalDateTime.now());

        Review savedReview = reviewRepository.save(foundReview);
        return new ReviewResponseDTO(savedReview);
    }

    @Transactional
    public void deleteReview(
            Long reviewId,
            User customer) {

        Review foundReview = reviewRepository.findById(reviewId)
                .orElseThrow(() -> new NotFoundException("Avaliação de id " + reviewId + " não encontrada"));

        if (!foundReview.getCustomer().getId().equals(customer.getId())) {
            throw new AccessDeniedException("Você não tem permissão para deletar esta avaliação.");
        }

        reviewRepository.delete(foundReview);
    }

}
