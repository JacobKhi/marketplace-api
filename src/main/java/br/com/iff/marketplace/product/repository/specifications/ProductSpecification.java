package br.com.iff.marketplace.product.repository.specifications;

import br.com.iff.marketplace.product.Product;
import jakarta.persistence.criteria.JoinType;
import org.springframework.data.jpa.domain.Specification;
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
        return (root, query, builder) -> {
            if (query != null) {
                query.distinct(true);
            }

            var variations = root.join("variations", JoinType.INNER);

            if (minPrice != null && maxPrice != null) {
                return builder.between(variations.get("price"), minPrice, maxPrice);
            }
            if (minPrice != null) {
                return builder.greaterThanOrEqualTo(variations.get("price"), minPrice);
            }
            if (maxPrice != null) {
                return builder.lessThanOrEqualTo(variations.get("price"), maxPrice);
            }
            return null;
        };
    }
}