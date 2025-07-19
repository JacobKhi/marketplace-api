package br.com.iff.marketplace.product.controller;

import br.com.iff.marketplace.controller.dto.*;
import br.com.iff.marketplace.product.Product;
import br.com.iff.marketplace.product.dto.ProductVariationRequestDTO;
import br.com.iff.marketplace.product.service.ProductService;
import br.com.iff.marketplace.product.dto.ProductRequestDTO;
import br.com.iff.marketplace.product.dto.ProductResponseDTO;
import br.com.iff.marketplace.service.ReviewService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import java.util.List;

import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import br.com.iff.marketplace.product.ProductVariation;
import org.springframework.web.bind.annotation.PathVariable;

@RestController
@RequestMapping("/produtos")
@Slf4j
@RequiredArgsConstructor
public class ProductController {

    private final ProductService service;
    private final ReviewService reviewService;

}