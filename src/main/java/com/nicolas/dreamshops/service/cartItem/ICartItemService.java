package com.nicolas.dreamshops.service.cartItem;

import com.nicolas.dreamshops.model.CartItem;

public interface ICartItemService {
    void addItemToCart(Long cartId, Long productId, int quantity);
    void updateItemQuantity(Long cartId, Long productId, int quantity);
    void removeItemFromCart(Long cartId, Long productId);
//  I would make this an optional instead of returning the CartItem directly
    CartItem getCartItemByProductId(Long cartId, Long productId);

//    HOW I WOULD DO THEM:
//    void updateItemQuantity(Long cartItemId, int quantity);
//    void removeItemFromCart(Long cartId, Long cartItemId);
}
