package com.nicolas.dreamshops.dto.cart;

import com.nicolas.dreamshops.dto.cartItem.CartItemInfo;
import lombok.Data;

import java.math.BigDecimal;
import java.util.Set;

@Data
public class CartDTO {
    private Long id;
    private BigDecimal totalPrice;
    private Set<CartItemInfo> cartItems;
}
