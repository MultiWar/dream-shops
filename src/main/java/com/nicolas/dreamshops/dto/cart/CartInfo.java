package com.nicolas.dreamshops.dto.cart;

import lombok.Data;

import java.math.BigDecimal;

@Data
public class CartInfo {
    private Long id;
    private BigDecimal totalPrice;
}
