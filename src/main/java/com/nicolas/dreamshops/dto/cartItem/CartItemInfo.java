package com.nicolas.dreamshops.dto.cartItem;

import com.nicolas.dreamshops.dto.product.ProductInfo;
import lombok.Data;

import java.math.BigDecimal;

@Data
public class CartItemInfo {
    private Long id;
    private int quantity;
    private BigDecimal unitPrice;
    private BigDecimal totalPrice;
    private ProductInfo product;
}
