package com.nicolas.dreamshops.dto.product;

import com.nicolas.dreamshops.dto.category.CategoryInfo;
import com.nicolas.dreamshops.dto.image.ImageDto;
import lombok.Data;

import java.math.BigDecimal;
import java.util.List;

@Data
public class ProductDto {
    private Long id;
    private String name;
    private String brand;
    private BigDecimal price;
    private int inventory;
    private String description;

    private CategoryInfo category;
    private List<ImageDto> images;
}
