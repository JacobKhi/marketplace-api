package br.com.iff.marketplace.review.dto;

import br.com.iff.marketplace.review.Review;
import lombok.Data;
import java.time.LocalDateTime;

@Data
public class ReviewResponseDTO {

    private Long id;
    private Integer rating;
    private String comment;
    private LocalDateTime reviewDate;
    private String reviewerName;
    private String orderNumber;
    private String sellerResponse;
    private LocalDateTime responseDate;

    public ReviewResponseDTO(Review review) {
        this.id = review.getId();
        this.rating = review.getRating();
        this.comment = review.getComment();
        this.reviewDate = review.getReviewDate();
        this.reviewerName = review.getCustomer().getName();
        this.orderNumber = review.getOrder().getOrderNumber();
        this.sellerResponse = review.getSellerResponse();
        this.responseDate = review.getResponseDate();
    }
}