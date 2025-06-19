package com.javaboy.mani.service.category;

import com.javaboy.mani.model.Category;
import com.javaboy.mani.repository.CategoryRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class CategoryService implements ICategoryService {
    private final CategoryRepository categoryRepository;
    @Override
    public Category addCategory(Category category) {
        return Optional.ofNullable(categoryRepository.findByName(category.getName()))
                .orElseGet(() -> categoryRepository.save(category));

    }

    @Override
    public Category updateCategory(Category category, Long id) {
        return categoryRepository.findById(id)
                .map(existingCategory -> {
                    existingCategory.setName(category.getName());
                    return categoryRepository.save(existingCategory);
                })
                .orElseThrow(() -> new RuntimeException("Category not found with id: " + id));

    }

    @Override
    public void deleteCategory(Long id) {
       categoryRepository.findById(id)
               .ifPresentOrElse(categoryRepository :: delete, () ->{
                     throw new RuntimeException("Category not found with id: " + id);
               });
    }

    @Override
    public List<Category> getAllCategories() {
        return categoryRepository.findAll();
    }

    @Override
    public Category findCategoryByName(String name) {
        return categoryRepository.findByName(name);
    }

    @Override
    public Category findCategoryById(Long id) {
        return categoryRepository.findById(id)
        .orElseThrow(() -> new RuntimeException("Category not found with id: " + id));
    }
}
