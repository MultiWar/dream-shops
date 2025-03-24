package com.nicolas.dreamshops.controller;

import com.nicolas.dreamshops.dto.category.CategoryDto;
import com.nicolas.dreamshops.exceptions.AlreadyExistsException;
import com.nicolas.dreamshops.exceptions.ResourceNotFoundException;
import com.nicolas.dreamshops.model.Category;
import com.nicolas.dreamshops.response.ApiResponse;
import com.nicolas.dreamshops.service.category.ICategoryService;
import lombok.RequiredArgsConstructor;
import org.apache.coyote.Response;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

import static org.springframework.http.HttpStatus.*;

@RestController
@RequestMapping("${api.prefix}/categories")
@RequiredArgsConstructor
public class CategoryController {
    private final ICategoryService categoryService;

    @GetMapping()
    public ResponseEntity<ApiResponse> getAllCategories() {
        try {
            List<Category> categories = categoryService.getAllCategories();
            List<CategoryDto> categoriesDtos = categoryService.getCategoriesDTOs(categories);

            return ResponseEntity.ok(new ApiResponse("", categoriesDtos));
        } catch (Exception e) {
            return ResponseEntity
                    .status(INTERNAL_SERVER_ERROR)
                    .body(new ApiResponse("Something went wrong", INTERNAL_SERVER_ERROR));
        }
    }

//    This is how I would do these Get requests. Having both GetById and GetByName
//    as different endpoints makes the paths needlessly convoluted imo.

//    @GetMapping("/category")
//    public ResponseEntity<ApiResponse> getCategory(@RequestParam(value = "id", required = false) Long id, @RequestParam(value = "name", required = false) String name) {
//        if(id == null && name == null) {
//            return ResponseEntity.status(BAD_REQUEST).body(new ApiResponse("Please provide either category id or name", null));
//        }
//
//        try {
//            Category category;
//
//            if(id != null) {
//                category = categoryService.getCategoryById(id);
//            } else {
//                category = categoryService.getCategoryByName(name);
//            }
//
//            return ResponseEntity.ok().body(new ApiResponse("", category))
//        } catch (ResourceNotFoundException e) {
//            return ResponseEntity.status(NOT_FOUND).body(new ApiResponse(e.getMessage(), null));
//        }
//    }

    @GetMapping("/category/{id}")
    public ResponseEntity<ApiResponse> getCategoryById(@PathVariable Long id) {
        try {
            Category category = categoryService.getCategoryById(id);
            CategoryDto categoryDto = categoryService.convertToDTO(category);
            return ResponseEntity.ok(new ApiResponse("", categoryDto));
        } catch (ResourceNotFoundException e) {
            return ResponseEntity.status(NOT_FOUND).body(new ApiResponse(e.getMessage(), null));
        }
    }

    @GetMapping("/categoryName/{name}")
    public ResponseEntity<ApiResponse> getCategoryByName(@PathVariable String name) {
        try {
            Category category = categoryService.getCategoryByName(name);
            CategoryDto categoryDto = categoryService.convertToDTO(category);
            return ResponseEntity.ok(new ApiResponse("", categoryDto));
        } catch (ResourceNotFoundException e) {
            return ResponseEntity.status(NOT_FOUND).body(new ApiResponse(e.getMessage(), null));
        }
    }

    @PostMapping("/add")
    public ResponseEntity<ApiResponse> addCategory(@RequestBody Category newCategory) {
        try {
            Category category = categoryService.addCategory(newCategory);
            CategoryDto categoryDto = categoryService.convertToDTO(category);
            return ResponseEntity.ok(new ApiResponse("Category added!", categoryDto));
        } catch (AlreadyExistsException e) {
            return ResponseEntity.status(CONFLICT).body(new ApiResponse(e.getMessage(), null));
        }
    }

    @PutMapping("/category/{id}/update")
    public ResponseEntity<ApiResponse> updateCategory(@RequestBody Category category, @PathVariable Long id) {
        try {
            Category updatedCategory = categoryService.updateCategory(category, id);
            CategoryDto categoryDto = categoryService.convertToDTO(updatedCategory);
            return ResponseEntity.ok(new ApiResponse("Category updated!", categoryDto));
        } catch (ResourceNotFoundException e) {
            return ResponseEntity.status(NOT_FOUND).body(new ApiResponse(e.getMessage(), null));
        }
    }

    @DeleteMapping("/category/{id}/delete")
    public ResponseEntity<ApiResponse> deleteCategory(@PathVariable Long id) {
        try {
            categoryService.deleteCategoryById(id);
            return ResponseEntity.ok(new ApiResponse("Category deleted.", null));
        } catch (Exception e) {
            return ResponseEntity.status(NOT_FOUND).body(new ApiResponse(e.getMessage(), null));
        }
    }
}
