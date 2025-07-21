package br.com.iff.marketplace.product.dto;

import br.com.iff.marketplace.category.dto.CategoryResponseDTO;
import br.com.iff.marketplace.user.dto.UserResponseDTO;
import br.com.iff.marketplace.product.Product;
import lombok.Data;

import java.util.List;
import java.util.stream.Collectors;


@Data
public class ProductResponseDTO {

    private Long id;

    private String name;

    private String description;

    private String brand;

    private Boolean sponsoredAd;

    private CategoryResponseDTO category;

    private UserResponseDTO seller;

    private List<ProductVariationResponseDTO> variations;

    public ProductResponseDTO(Product product) {

        this.id = product.getId();
        this.name = product.getName();
        this.description = product.getDescription();
        this.brand = product.getBrand();
        this.sponsoredAd = product.getSponsoredAd();
        this.category = new CategoryResponseDTO(product.getCategory());
        this.seller = new UserResponseDTO(product.getSeller());
        this.variations = product.getVariations().stream()
                .map(ProductVariationResponseDTO::new)
                .collect(Collectors.toList());

    }

}