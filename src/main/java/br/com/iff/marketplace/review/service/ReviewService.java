package br.com.iff.marketplace.review.service;

import br.com.iff.marketplace.order.Order;
import br.com.iff.marketplace.product.Product;
import br.com.iff.marketplace.product.repository.ProductRepository;
import br.com.iff.marketplace.review.Review;
import br.com.iff.marketplace.review.dto.ReviewRequestDTO;
import br.com.iff.marketplace.review.dto.ReviewResponseDTO;
import br.com.iff.marketplace.review.repository.ReviewRepository;
import br.com.iff.marketplace.user.User;
import br.com.iff.marketplace.order.repository.OrderRepository;
import br.com.iff.marketplace.user.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import br.com.iff.marketplace.exception.NotFoundException;
import java.time.LocalDateTime;

@Service
@RequiredArgsConstructor
public class ReviewService {

    private final ReviewRepository reviewRepository;
    private final OrderRepository orderRepository;
    private final UserRepository userRepository;
    private final ProductRepository productRepository;

    @Transactional
    public ReviewResponseDTO createReview(ReviewRequestDTO reviewDTO, User customer) {

        Product foundProduct = productRepository.findById(reviewDTO.getProductId())
                .orElseThrow(() -> new NotFoundException("Produto não encontrado"));

        Order foundOrder = orderRepository.findById(reviewDTO.getOrderId())
                .orElseThrow(() -> new NotFoundException("Pedido não encontrado"));

        if (!foundOrder.getCustomer().getId().equals(customer.getId())) {
            throw new AccessDeniedException("Você só pode avaliar pedidos que você fez.");
        }

        if (reviewRepository.existsByCustomerIdAndProductId(customer.getId(), foundProduct.getId())) {
            throw new IllegalStateException("Voce já avaliou esse produto");
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
    public ReviewResponseDTO addSellerResponse(Long reviewId, String response, User seller) {

        Review foundReview = reviewRepository.findById(reviewId)
                .orElseThrow(() -> new NotFoundException("Avalicão não encontrada"));

        if(!foundReview.getProduct().getSeller().getId().equals(seller.getId())) {
            throw new AccessDeniedException("Você só pode responder avaliações de seus próprios produtos");
        }

        foundReview.setSellerResponse(response);
        foundReview.setResponseDate(LocalDateTime.now());

        Review savedReview = reviewRepository.save(foundReview);

        return new ReviewResponseDTO(savedReview);
    }

    public Page<ReviewResponseDTO> listByProducts(
            Long ProductId,
            Pageable pageable) {

        Page<Review> reviewsPage = reviewRepository.findByProductId(ProductId, pageable);
        return reviewsPage.map(ReviewResponseDTO::new);
    }

}