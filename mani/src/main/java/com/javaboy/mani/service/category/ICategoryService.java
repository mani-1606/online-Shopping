package com.javaboy.mani.service.category;

import com.javaboy.mani.model.Category;

import java.util.List;

public interface ICategoryService {
    Category addCategory(Category category);
    Category updateCategory(Category category, Long id);
    void deleteCategory(Long id);
    List<Category> getAllCategories();
    Category findCategoryByName(String name);
    Category findCategoryById(Long id);


}
