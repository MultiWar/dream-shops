package com.nicolas.dreamshops.dto.product;

import lombok.Data;

import java.math.BigDecimal;

@Data
public class ProductInfo {
    private Long id;
    private String name;
    private String brand;
    private BigDecimal price;
    private int inventory;
    private String description;
}
