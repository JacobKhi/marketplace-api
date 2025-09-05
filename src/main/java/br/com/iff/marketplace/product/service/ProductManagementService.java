package br.com.iff.marketplace.product.service;

import br.com.iff.marketplace.category.Category;
import br.com.iff.marketplace.category.repository.CategoryRepository;
import br.com.iff.marketplace.exception.NotFoundException;
import br.com.iff.marketplace.product.Product;
import br.com.iff.marketplace.product.ProductVariation;
import br.com.iff.marketplace.product.dto.ProductRequestDTO;
import br.com.iff.marketplace.product.dto.ProductResponseDTO;
import br.com.iff.marketplace.product.dto.UpdateProductDTO;
import br.com.iff.marketplace.product.repository.ProductRepository;
import br.com.iff.marketplace.user.User;
import br.com.iff.marketplace.user.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class ProductManagementService {

    private final ProductRepository productRepository;
    private final CategoryRepository categoryRepository;
    private final UserRepository userRepository;

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
                .orElseThrow(() -> new NotFoundException("Categoria com ID " + requestDTO.getCategoryId() + " não encontrada!"));

        User seller = userRepository.findById(sellerId)
                .orElseThrow(() -> new NotFoundException("Vendedor com ID " + sellerId  + " não encontrado!"));

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
                .orElseThrow(() -> new NotFoundException("Produto com ID " + productId + " não encontrado!"));

        if (!foundProduct.getSeller().getId().equals(sellerId)) {
            throw new AccessDeniedException("Acesso negado: Você só pode editar seus próprios produtos.");
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
                .orElseThrow(() -> new NotFoundException("Produto com ID " + productId + " não encontrado!"));

        if (!foundProduct.getSeller().getId().equals(sellerId)) {
            throw new AccessDeniedException("Acesso negado: Você só pode deletar seus próprios produtos.");
        }

        productRepository.deleteById(productId);
    }

}