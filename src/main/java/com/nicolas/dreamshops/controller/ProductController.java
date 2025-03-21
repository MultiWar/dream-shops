package com.nicolas.dreamshops.controller;

import com.nicolas.dreamshops.exceptions.ResourceNotFoundException;
import com.nicolas.dreamshops.model.Product;
import com.nicolas.dreamshops.request.AddProductRequest;
import com.nicolas.dreamshops.request.UpdateProductRequest;
import com.nicolas.dreamshops.response.ApiResponse;
import com.nicolas.dreamshops.service.product.IProductService;
import lombok.RequiredArgsConstructor;
import org.apache.coyote.Response;
import org.springframework.core.io.Resource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.sql.SQLException;
import java.util.List;

import static org.springframework.http.HttpStatus.INTERNAL_SERVER_ERROR;
import static org.springframework.http.HttpStatus.NOT_FOUND;

@RestController
@RequestMapping("${api.prefix}/products")
@RequiredArgsConstructor
public class ProductController {
    private final IProductService productService;

    @GetMapping()
    public ResponseEntity<ApiResponse> getAllProducts() {
        List<Product> products = productService.getAllProducts();
        return ResponseEntity.ok(new ApiResponse("", products));
    }

    @GetMapping("/product/{id}")
    public ResponseEntity<ApiResponse> getProductById(@PathVariable Long id) {
        try {
            Product product = productService.getProductById(id);
            return ResponseEntity.ok(new ApiResponse("", product));
        } catch (ResourceNotFoundException e) {
            return ResponseEntity.status(NOT_FOUND).body(new ApiResponse(e.getMessage(), null));
        }
    }

//  this is my version of how I see the multiple parametrized gets in this controller
//  (I do see the value in having a route /products/category?productName though)

//    @GetMapping("/query")
//    public ResponseEntity<ApiResponse> queryProduct(
//            @RequestParam(value = "brand", required = false) String brand,
//            @RequestParam(value = "name", required = false) String name
//    ) {
//        if(brand == null && name == null) {
//            return getAllProducts();
//        }
//
//        List<Product> products = null;
//
//        try {
//            if(brand != null && name != null) {
//                products = productService.getProductsByBrandAndName(brand, name);
//            }
//
//            if(brand != null && name == null) {
//                products = productService.getProductsByBrand(brand);
//            }
//
//            if(brand == null && name != null) {
//                products = productService.getProductByName(name);
//            }
//
//            return ResponseEntity.ok(new ApiResponse("", products));
//        } catch (Exception e) {
//            return ResponseEntity.status(INTERNAL_SERVER_ERROR).body(new ApiResponse(e.getMessage(), null));
//        }
//    }

    @GetMapping("/by/name")
    public ResponseEntity<ApiResponse> getProductsByName(@RequestParam String name) {
        try {
            List<Product> products = productService.getProductByName(name);
            if(products.isEmpty()) {
                return ResponseEntity.status(NOT_FOUND).body(new ApiResponse("No products found", null));
            }

            return ResponseEntity.ok(new ApiResponse("", products));
        } catch (Exception e) {
            return ResponseEntity.status(INTERNAL_SERVER_ERROR).body(new ApiResponse(e.getMessage(), null));
        }
    }

    @GetMapping("/by/brand")
    public ResponseEntity<ApiResponse> getProductsByBrand(@RequestParam String brand) {
        try {
            List<Product> products = productService.getProductsByBrand(brand);
            if(products.isEmpty()) {
                return ResponseEntity.status(NOT_FOUND).body(new ApiResponse("No products found", null));
            }

            return ResponseEntity.ok(new ApiResponse("", products));
        } catch (Exception e) {
            return ResponseEntity.status(INTERNAL_SERVER_ERROR).body(new ApiResponse(e.getMessage(), null));
        }
    }

    @GetMapping("/by/brand-and-name")
    public ResponseEntity<ApiResponse> getProductsByBrandAndName(@RequestParam String brand, @RequestParam String name) {
        try {
            List<Product> products = productService.getProductsByBrandAndName(brand, name);
            if(products.isEmpty()) {
                return ResponseEntity.status(NOT_FOUND).body(new ApiResponse("No products found", null));
            }

            return ResponseEntity.ok(new ApiResponse("", products));
        } catch (Exception e) {
            return ResponseEntity.status(INTERNAL_SERVER_ERROR).body(new ApiResponse(e.getMessage(), null));
        }
    }

    @GetMapping("/{category}")
    public ResponseEntity<ApiResponse> getProductsByCategory(@PathVariable String category) {
        try {
            List<Product> products = productService.getProductsByCategoryName(category);
            if(products.isEmpty()) {
                return ResponseEntity.status(NOT_FOUND).body(new ApiResponse("Products not found", null));
            }

            return ResponseEntity.ok(new ApiResponse("", products));
        } catch (Exception e) {
            return ResponseEntity.status(INTERNAL_SERVER_ERROR).body(new ApiResponse(e.getMessage(), null));
        }
    }

    @GetMapping("/by/category-and-name")
    public ResponseEntity<ApiResponse> getProductsByCategoryAndBrand(@RequestParam String category, @RequestParam String brand) {
        try {
            List<Product> products = productService.getProductsByCategoryAndBrand(category, brand);
            if(products.isEmpty()) {
                return ResponseEntity.status(NOT_FOUND).body(new ApiResponse("No products found", null));
            }

            return ResponseEntity.ok(new ApiResponse("", products));
        } catch (Exception e) {
            return ResponseEntity.status(INTERNAL_SERVER_ERROR).body(new ApiResponse(e.getMessage(), null));
        }
    }

    @PostMapping("/product/create")
    public ResponseEntity<ApiResponse> addProduct(@RequestBody AddProductRequest productRequest) {
        try {
            Product product = productService.addProduct(productRequest);
            return ResponseEntity.ok(new ApiResponse("Added product successfully!", product));
        } catch (Exception e) {
            return ResponseEntity.status(INTERNAL_SERVER_ERROR).body(new ApiResponse(e.getMessage(), null));
        }
    }

    @PutMapping("/product/{id}/update")
    public ResponseEntity<ApiResponse> updateProduct(@RequestBody UpdateProductRequest productRequest, @PathVariable Long id) {
        try {
            Product product = productService.updateProduct(productRequest, id);
            return ResponseEntity.ok(new ApiResponse("Product updated!", product));
        } catch (ResourceNotFoundException e) {
            return ResponseEntity.status(NOT_FOUND).body(new ApiResponse(e.getMessage(), null));
        }
    }

    @DeleteMapping("/product/{id}/delete")
    public ResponseEntity<ApiResponse> deleteProduct(@PathVariable Long id) {
        try {
            productService.deleteProductById(id);
            return ResponseEntity.ok(new ApiResponse("Product deleted.", null));
        } catch (ResourceNotFoundException e) {
            return ResponseEntity.ok(new ApiResponse(e.getMessage(), null));
        }
    }
}
