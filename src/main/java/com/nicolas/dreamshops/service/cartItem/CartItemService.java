package com.nicolas.dreamshops.service.cartItem;

import com.nicolas.dreamshops.exceptions.ResourceNotFoundException;
import com.nicolas.dreamshops.model.Cart;
import com.nicolas.dreamshops.model.CartItem;
import com.nicolas.dreamshops.model.Product;
import com.nicolas.dreamshops.repository.CartItemRepository;
import com.nicolas.dreamshops.repository.CartRepository;
import com.nicolas.dreamshops.service.cart.ICartService;
import com.nicolas.dreamshops.service.product.IProductService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class CartItemService implements ICartItemService {
    private final CartItemRepository cartItemRepository;
    private final CartRepository cartRepository;
    private final ICartService cartService;
    private final IProductService productService;

    @Override
    public void addItemToCart(Long cartId, Long productId, int quantity) {
        Cart cart = cartService.getCart(cartId);
        Product product = productService.getProductById(productId);

        CartItem cartItem = cart.getCartItems().stream()
                .filter(item -> item.getProduct().getId().equals(productId))
                .findFirst().orElse(new CartItem());

        if(cartItem.getId() == null) {
            cartItem.setCart(cart);
            cartItem.setProduct(product);
            cartItem.setQuantity(quantity);
            cartItem.setUnitPrice(product.getPrice());
        } else {
            cartItem.setQuantity(cartItem.getQuantity() + quantity);
        }

        cartItem.setTotalPrice();
        cart.addItem(cartItem);

        cartItemRepository.save(cartItem);
        cartRepository.save(cart);
    }

    @Override
    public void updateItemQuantity(Long cartId, Long productId, int quantity) {
        Cart cart = cartService.getCart(cartId);
        cart.getCartItems().stream()
                .filter(item -> item.getProduct().getId().equals(productId))
                .findFirst()
                .ifPresent(item -> {
                    item.setQuantity(quantity);
                    item.setUnitPrice(item.getProduct().getPrice());
                    item.setTotalPrice();
                    cartItemRepository.save(item);
                });

        cart.updateTotalPrice();
        cartRepository.save(cart);
    }

    @Override
    public void removeItemFromCart(Long cartId, Long productId) {
        Cart cart = cartService.getCart(cartId);
        CartItem cartItem = getCartItemByProductId(cartId, productId);

        cart.removeItem(cartItem);
        cartRepository.save(cart);
    }


//    HOW I WOULD DO THEM
//    @Override
//    public void updateItemQuantity(Long cartItemId, int quantity) {
//        CartItem cartItem = cartItemRepository
//                .findById(cartItemId)
//                .orElseThrow(() -> new ResourceNotFoundException("Cart Item not found"));

//        Cart cart = cartItem.getCart();
//
//        cartItem.setQuantity(quantity);
//        cartItem.setUnitPrice(cartItem.getProduct().getPrice());
//        cartItem.setTotalPrice();
//        cartItemRepository.save(cartItem);
//        cart.updateTotalPrice();
//        cartRepository.save(cart);
//    }
//
//    @Override
//    public void removeItemFromCart(Long cartId, Long cartItemId) {
//        Cart cart = cartService.getCart(cartId);
//        CartItem cartItem = cart.getCartItems().stream()
//                .filter(item -> item.getId().equals(cartItemId))
//                .findFirst().orElseThrow(() -> new ResourceNotFoundException("Cart item not found"));
//
//        cart.removeItem(cartItem);
//        cartRepository.save(cart);
//    }

//  I'd rather this was an Optional, so I could use this in more places
    @Override
    public CartItem getCartItemByProductId(Long cartId, Long productId) {
        Cart cart = cartService.getCart(cartId);
        return cart.getCartItems()
                .stream()
                .filter(item -> item.getProduct().getId().equals(productId))
                .findFirst().orElseThrow(() -> new ResourceNotFoundException("Product not found in cart"));
    }
}
