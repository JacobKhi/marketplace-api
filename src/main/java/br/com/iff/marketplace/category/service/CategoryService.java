package br.com.iff.marketplace.category.service;

import br.com.iff.marketplace.category.Category;
import br.com.iff.marketplace.category.repository.CategoryRepository;
import br.com.iff.marketplace.category.dto.CategoryResponseDTO;
import br.com.iff.marketplace.category.dto.CreateCategoryDTO;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class CategoryService {

    private final CategoryRepository categoryRepository;

    public CategoryResponseDTO createCategory(CreateCategoryDTO categoryDTO) {
        // Por enquanto, a lógica é simples: apenas salvar.
        // Futuramente, poderíamos adicionar validações aqui (ex: verificar se o nome já existe)
        Category category = new Category();
        category.setName(categoryDTO.getName());
        category.setDescription(categoryDTO.getDescription());

        Category savedCategory = categoryRepository.save(category);

        return new CategoryResponseDTO(savedCategory);
    }

    public List<CategoryResponseDTO> findAll() {
        return categoryRepository.findAll()
                .stream()
                .map(category -> new CategoryResponseDTO(category))
                .collect(Collectors.toList());
    }
}