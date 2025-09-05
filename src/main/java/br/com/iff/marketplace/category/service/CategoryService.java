package br.com.iff.marketplace.category.service;

import br.com.iff.marketplace.category.Category;
import br.com.iff.marketplace.category.repository.CategoryRepository;
import br.com.iff.marketplace.category.dto.CategoryResponseDTO;
import br.com.iff.marketplace.category.dto.CreateCategoryDTO;
import br.com.iff.marketplace.exception.NotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class CategoryService {

    private final CategoryRepository categoryRepository;

    public CategoryResponseDTO findById(Long categoryId) {

        Category foundCategory = categoryRepository.findById(categoryId)
                .orElseThrow(() -> new NotFoundException("Categoria com ID " + categoryId + " não encontrada."));
        return new CategoryResponseDTO(foundCategory);
    }

    public Page<CategoryResponseDTO> findAll(Pageable pageable) {

        Page<Category> categoriesPage = categoryRepository.findAll(pageable);
        return categoriesPage.map(CategoryResponseDTO::new);
    }

    public CategoryResponseDTO createCategory(CreateCategoryDTO categoryDTO) {

        Category category = new Category();
        category.setName(categoryDTO.getName());
        category.setDescription(categoryDTO.getDescription());

        Category savedCategory = categoryRepository.save(category);
        return new CategoryResponseDTO(savedCategory);
    }

    @Transactional
    public CategoryResponseDTO updateCategory(Long categoryId, CreateCategoryDTO categoryDTO) {

        Category foundCategory = categoryRepository.findById(categoryId)
                .orElseThrow(() -> new NotFoundException("Categoria com ID " + categoryId + " não encontrada."));

        foundCategory.setName(categoryDTO.getName());
        foundCategory.setDescription(categoryDTO.getDescription());

        Category updatedCategory = categoryRepository.save(foundCategory);
        return new CategoryResponseDTO(updatedCategory);
    }

    @Transactional
    public void deleteCategory(Long categoryId) {

        if (!categoryRepository.existsById(categoryId)) {
            throw new NotFoundException("Categoria com ID " + categoryId + " não encontrada.");
        }
        categoryRepository.deleteById(categoryId);
    }

}