package br.com.iff.marketplace.product.service;

import br.com.iff.marketplace.exception.NotFoundException;
import br.com.iff.marketplace.product.Product;
import br.com.iff.marketplace.product.dto.ProductResponseDTO;
import br.com.iff.marketplace.product.repository.ProductRepository;
import br.com.iff.marketplace.product.repository.specifications.ProductSpecification;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;

import java.math.BigDecimal;

@Service
@RequiredArgsConstructor
public class ProductSearchService {

    private final ProductRepository productRepository;

    public ProductResponseDTO findProductById(Long productId) {

        Product foundProduct = productRepository.findById(productId)
                .orElseThrow(() -> new NotFoundException("Produto com ID " + productId + " não encontrado!"));

        return new ProductResponseDTO(foundProduct);
    }

    public Page<ProductResponseDTO> searchProducts(
            String name,
            Long categoryId,
            BigDecimal minPrice,
            BigDecimal maxPrice,
            Pageable pageable) {

        Specification<Product> spec = (root, query, builder) -> builder.conjunction();

        if (name != null && !name.isBlank()) {
            spec = spec.and(ProductSpecification.withName(name));
        }
        if (categoryId != null) {
            spec = spec.and(ProductSpecification.withCategory(categoryId));
        }
        if (minPrice != null || maxPrice != null) {
            spec = spec.and(ProductSpecification.priceBetween(minPrice, maxPrice));
        }

        Sort sponsoredSort = Sort.by(Sort.Direction.DESC, "sponsoredAd");
        Sort finalSort = sponsoredSort.and(pageable.getSort());
        Pageable finalPageable = PageRequest.of(pageable.getPageNumber(), pageable.getPageSize(), finalSort);

        Page<Product> productsPage = productRepository.findAll(spec, finalPageable);
        return productsPage.map(ProductResponseDTO::new);
    }

}