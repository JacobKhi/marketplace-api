package br.com.iff.marketplace.product.repository.specifications;

import org.springframework.data.jpa.domain.Specification;
import br.com.iff.marketplace.product.Product;

import java.math.BigDecimal;

public class ProductSpecification {

    public static Specification<Product> withName(String name) {
        if (name == null || name.isBlank()) {
            return null;
        }

        return (root, query, builder) ->
                builder.like(builder.lower(root.get("name")), "%" + name.toLowerCase() + "%");
    }

    public static Specification<Product> withCategory(Long categoryId) {
        if (categoryId == null) {
            return null;
        }

        return (root, query, builder) ->
                builder.equal(root.get("category").get("id"), categoryId);
    }

    public static Specification<Product> priceBetween(BigDecimal minPrice, BigDecimal maxPrice) {
        if (minPrice != null && maxPrice != null) {
            return (root, query, builder) -> builder.between(root.get("price"), minPrice, maxPrice);
        }
        if (minPrice != null) {
            return (root, query, builder) -> builder.greaterThanOrEqualTo(root.get("price"), minPrice);
        }
        if (maxPrice != null) {
            return (root, query, builder) -> builder.lessThanOrEqualTo(root.get("price"), maxPrice);
        }
        return null;
    }

}