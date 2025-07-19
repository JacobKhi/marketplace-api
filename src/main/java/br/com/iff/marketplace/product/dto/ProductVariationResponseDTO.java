package br.com.iff.marketplace.product.dto;

import br.com.iff.marketplace.product.ProductVariation;
import lombok.Data;

import java.math.BigDecimal;

@Data
public class ProductVariationResponseDTO {

    private Long id;

    private String name;

    private BigDecimal price;

    private Integer stock;

    public ProductVariationResponseDTO(ProductVariation productVariation) {

        this.id = productVariation.getId();
        this.name = productVariation.getName();
        this.price = productVariation.getPrice();
        this.stock = productVariation.getStock();

    }

}
