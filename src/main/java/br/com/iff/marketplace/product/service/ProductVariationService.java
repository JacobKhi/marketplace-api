package br.com.iff.marketplace.product.service;

import br.com.iff.marketplace.exception.NotFoundException;
import br.com.iff.marketplace.product.Product;
import br.com.iff.marketplace.product.ProductVariation;
import br.com.iff.marketplace.product.dto.ProductVariationRequestDTO;
import br.com.iff.marketplace.product.dto.ProductVariationResponseDTO;
import br.com.iff.marketplace.product.repository.ProductRepository;
import br.com.iff.marketplace.product.repository.ProductVariationRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class ProductVariationService {

    private final ProductRepository productRepository;
    private final ProductVariationRepository productVariationRepository;

    public ProductVariationResponseDTO addVariation(
            Long productId,
            ProductVariationRequestDTO variationDTO,
            Long sellerId) {

        Product rootProduct = productRepository.findById(productId)
                .orElseThrow(() -> new NotFoundException("Produto com ID " + productId + " não encontrado!"));

        if (!rootProduct.getSeller().getId().equals(sellerId)) {
            throw new AccessDeniedException("Acesso negado: Você só pode adicionar variações aos seus próprios produtos.");
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
                .orElseThrow(() -> new NotFoundException("Variação com ID " + variationId + " não encontrado!"));

        if (!foundVariation.getProduct().getId().equals(productId)) {
            throw new IllegalStateException("Esta variação não pertence ao produto informado.");
        }

        if (!foundVariation.getProduct().getSeller().getId().equals(sellerId)) {
            throw new AccessDeniedException("Acesso negado: Você só pode editar as variações de seus próprios produtos.");
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
                .orElseThrow(() -> new NotFoundException("Variação com ID " + variationId + " não encontrado!"));

        if (!foundVariation.getProduct().getId().equals(productId)) {
            throw new IllegalStateException("Esta variação não pertence ao produto informado.");
        }

        if (!foundVariation.getProduct().getSeller().getId().equals(sellerId)) {
            throw new AccessDeniedException("Acesso negado: Você só pode deletar as variações de seus próprios produtos.");
        }

        productVariationRepository.delete(foundVariation);
    }

}