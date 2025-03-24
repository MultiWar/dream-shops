package com.nicolas.dreamshops.dto.category;

import com.nicolas.dreamshops.dto.product.ProductInfo;
import lombok.Data;

import java.util.List;

@Data
public class CategoryDto {
    private Long id;
    private String name;

    private List<ProductInfo> products;
}
