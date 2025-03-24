package com.nicolas.dreamshops.service.category;

import com.nicolas.dreamshops.dto.category.CategoryDto;
import com.nicolas.dreamshops.exceptions.AlreadyExistsException;
import com.nicolas.dreamshops.exceptions.ResourceNotFoundException;
import com.nicolas.dreamshops.model.Category;
import com.nicolas.dreamshops.repository.CategoryRepository;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class CategoryService implements ICategoryService {
    private final CategoryRepository categoryRepository;
    private final ModelMapper modelMapper;


    @Override
    public List<Category> getAllCategories() {
        return categoryRepository.findAll();
    }

    @Override
    public Category getCategoryById(Long id) {
        return categoryRepository.findById(id).orElseThrow(() -> new ResourceNotFoundException("Category not found!"));
    }

    @Override
    public Category getCategoryByName(String name) {
        return categoryRepository.findByName(name);
    }

    @Override
    public Category addCategory(Category category) {
        return Optional.of(category).filter(c -> !categoryRepository.existsByName(c.getName()))
                .map(categoryRepository::save).orElseThrow(() -> new AlreadyExistsException("Category already exists!"));
    }

    @Override
    public Category updateCategory(Category category, Long id) {
//      the guy from the video's approach:
        return Optional.ofNullable(getCategoryById(id)).map(oldCategory -> {
            category.setName(oldCategory.getName());
            return categoryRepository.save(category);
        }).orElseThrow(() -> new ResourceNotFoundException("Category not found!"));

//        what would be my approach:
//        categoryRepository.findById(id).ifPresentOrElse((oldCategory) -> {
//            category.setName(oldCategory.getName());
//            categoryRepository.save(category);
//        }, () -> {
//            throw new ResourceNotFoundException("Category not found!");
//        });
//
//        return category;
    }

    @Override
    public void deleteCategoryById(Long id) {
        categoryRepository.findById(id)
                .ifPresentOrElse(categoryRepository::delete, () -> {
                    throw new ResourceNotFoundException("Category not found!");
                });
    }

    @Override
    public CategoryDto convertToDTO(Category category) {
        return modelMapper.map(category, CategoryDto.class);
    }

    @Override
    public List<CategoryDto> getCategoriesDTOs(List<Category> categories) {
        return categories.stream().map(this::convertToDTO).toList();
    }
}
