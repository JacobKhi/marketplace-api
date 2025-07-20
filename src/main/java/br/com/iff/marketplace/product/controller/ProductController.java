package br.com.iff.marketplace.product.controller;

import br.com.iff.marketplace.product.service.ProductService;
import br.com.iff.marketplace.review.service.ReviewService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import lombok.extern.slf4j.Slf4j;

@RestController
@RequestMapping("/produtos")
@Slf4j
@RequiredArgsConstructor
public class ProductController {

    private final ProductService service;
    private final ReviewService reviewService;

}