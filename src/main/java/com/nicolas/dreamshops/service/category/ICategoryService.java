package com.nicolas.dreamshops.service.category;

import com.nicolas.dreamshops.dto.category.CategoryDto;
import com.nicolas.dreamshops.model.Category;

import java.util.List;

public interface ICategoryService {
    List<Category> getAllCategories();
    Category getCategoryById(Long id);
    Category getCategoryByName(String name);
    Category addCategory(Category category);
    Category updateCategory(Category category, Long id);
    void deleteCategoryById(Long id);

    CategoryDto convertToDTO(Category category);

    List<CategoryDto> getCategoriesDTOs(List<Category> categories);
}
