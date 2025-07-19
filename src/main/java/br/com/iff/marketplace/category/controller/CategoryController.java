package br.com.iff.marketplace.category.controller;

import br.com.iff.marketplace.category.Category;
import br.com.iff.marketplace.category.dto.CategoryResponseDTO;
import br.com.iff.marketplace.category.dto.CreateCategoryDTO;
import br.com.iff.marketplace.category.service.CategoryService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.util.List;

@RestController
@RequestMapping("api/v1/categories")
@RequiredArgsConstructor
public class CategoryController {

    private final CategoryService categoryService;

    // Endpoint para CADASTRAR uma nova categoria
    @PostMapping
    public ResponseEntity<CategoryResponseDTO> createCategory(@RequestBody @Valid CreateCategoryDTO categoryDTO) {
        CategoryResponseDTO createdCategory = categoryService.createCategory(categoryDTO);
        return ResponseEntity.status(HttpStatus.CREATED).body(createdCategory);
    }

    // Endpoint para LISTAR todas as categorias
    @GetMapping
    public ResponseEntity<List<CategoryResponseDTO>> findAll() {
        List<CategoryResponseDTO> categories = categoryService.findAll();
        return ResponseEntity.ok(categories);
    }

}