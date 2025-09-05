package br.com.iff.marketplace.product.service;

import br.com.iff.marketplace.product.Product;
import br.com.iff.marketplace.product.repository.ProductRepository;
import br.com.iff.marketplace.product.ProductVariation;
import br.com.iff.marketplace.product.repository.ProductVariationRepository;
import br.com.iff.marketplace.product.dto.*;
import br.com.iff.marketplace.category.Category;
import br.com.iff.marketplace.user.User;
import br.com.iff.marketplace.category.repository.CategoryRepository;
import br.com.iff.marketplace.user.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import java.util.List;
import java.util.stream.Collectors;
import org.springframework.data.jpa.domain.Specification;
import br.com.iff.marketplace.product.repository.specifications.ProductSpecification;
import org.springframework.transaction.annotation.Transactional;
import java.math.BigDecimal;

@Service
@Slf4j
@RequiredArgsConstructor
public class ProductService {

    private final ProductRepository productRepository;
    private final CategoryRepository categoryRepository;
    private final UserRepository userRepository;
    private final ProductVariationRepository productVariationRepository;

    public ProductResponseDTO findProductById(Long productId) {

        Product foundProduct = productRepository.findById(productId)
                .orElseThrow(() -> new RuntimeException("Produto não encontrado!"));

        return new ProductResponseDTO(foundProduct);
    }

    public Page<ProductResponseDTO> findProductBySeller(
            Long sellerId,
            Pageable pageable) {

        Page<Product> products = productRepository.findBySellerId(sellerId, pageable);
        return products.map(ProductResponseDTO::new);
    }

    public ProductResponseDTO createProduct(
            ProductRequestDTO requestDTO,
            Long sellerId) {

        Category category = categoryRepository.findById(requestDTO.getCategoryId())
                .orElseThrow(() -> new RuntimeException("Categoria não encontrada!"));

        User seller = userRepository.findById(sellerId)
                .orElseThrow(() -> new RuntimeException("Vendedor não encontrado!"));

        Product newProduct = new Product();
        newProduct.setName(requestDTO.getName());
        newProduct.setDescription(requestDTO.getDescription());
        newProduct.setBrand(requestDTO.getBrand());
        newProduct.setCategory(category);
        newProduct.setSeller(seller);

        ProductVariation variation = new ProductVariation();
        variation.setName(requestDTO.getVariationName());
        variation.setStock(requestDTO.getStock());
        variation.setPrice(requestDTO.getPrice());
        variation.setProduct(newProduct);

        newProduct.getVariations().add(variation);

        Product savedProduct = productRepository.save(newProduct);
        return new ProductResponseDTO(savedProduct);
    }

    public ProductResponseDTO updateProduct(
            Long productId,
            UpdateProductDTO updateDTO,
            Long sellerId) {

        Product foundProduct = productRepository.findById(productId)
                .orElseThrow(() -> new RuntimeException("Produto não encontrado!"));

        if (!foundProduct.getSeller().getId().equals(sellerId)) {
            throw new RuntimeException("Acesso negado: Você só pode editar seus próprios produtos.");
        }

        foundProduct.setName(updateDTO.getName());
        foundProduct.setDescription(updateDTO.getDescription());
        foundProduct.setBrand(updateDTO.getBrand());
        foundProduct.setSponsoredAd(updateDTO.getSponsoredAd());

        Product savedProduct = productRepository.save(foundProduct);
        return new ProductResponseDTO(savedProduct);
    }

    public void deleteProduct(
            Long productId,
            Long sellerId) {

        Product foundProduct = productRepository.findById(productId)
                .orElseThrow(() -> new RuntimeException("Produto não encontrado!"));

        if (!foundProduct.getSeller().getId().equals(sellerId)) {
            throw new RuntimeException("Acesso negado: Você só pode deletar seus próprios produtos.");
        }

        productRepository.deleteById(productId);
    }

    public ProductVariationResponseDTO addVariation(
            Long productId,
            ProductVariationRequestDTO variationDTO,
            Long sellerId) {

        Product rootProduct = productRepository.findById(productId)
                .orElseThrow(() -> new RuntimeException("Produto não encontrado!"));

        if (!rootProduct.getSeller().getId().equals(sellerId)) {
            throw new RuntimeException("Acesso negado: Você só pode adicionar variações aos seus próprios produtos.");
        }

        ProductVariation newVariation = new ProductVariation();
        newVariation.setName(variationDTO.getName());
        newVariation.setSku(variationDTO.getSku());
        newVariation.setPrice(variationDTO.getPrice());
        newVariation.setStock(variationDTO.getStock());
        newVariation.setProduct(rootProduct);

        rootProduct.getVariations().add(newVariation);

        ProductVariation savedVariation = productVariationRepository.save(newVariation);
        return new ProductVariationResponseDTO(savedVariation);
    }

    @Transactional
    public ProductVariationResponseDTO updateVariation(
            Long productId,
            Long variationId,
            ProductVariationRequestDTO variationDTO,
            Long sellerId) {

        ProductVariation foundVariation = productVariationRepository.findById(variationId)
                .orElseThrow(() -> new RuntimeException("Variação não encontrada!"));

        if (!foundVariation.getProduct().getId().equals(productId)) {
            throw new RuntimeException("Esta variação não pertence ao produto informado.");
        }

        if (!foundVariation.getProduct().getSeller().getId().equals(sellerId)) {
            throw new RuntimeException("Acesso negado: Você só pode editar as variações de seus próprios produtos.");
        }

        foundVariation.setName(variationDTO.getName());
        foundVariation.setSku(variationDTO.getSku());
        foundVariation.setPrice(variationDTO.getPrice());
        foundVariation.setStock(variationDTO.getStock());

        ProductVariation savedVariation = productVariationRepository.save(foundVariation);
        return new ProductVariationResponseDTO(savedVariation);
    }

    @Transactional
    public void deleteVariation(
            Long productId,
            Long variationId,
            Long sellerId) {

        ProductVariation foundVariation = productVariationRepository.findById(variationId)
                .orElseThrow(() -> new RuntimeException("Variação não encontrada!"));

        if (!foundVariation.getProduct().getId().equals(productId)) {
            throw new RuntimeException("Esta variação não pertence ao produto informado.");
        }

        if (!foundVariation.getProduct().getSeller().getId().equals(sellerId)) {
            throw new RuntimeException("Acesso negado: Você só pode deletar as variações de seus próprios produtos.");
        }

        productVariationRepository.delete(foundVariation);
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

        Page<Product> productsPage = productRepository.findAll(spec, pageable);
        return productsPage.map(ProductResponseDTO::new);
    }

}