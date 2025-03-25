package com.nicolas.dreamshops.service.cart;

import com.nicolas.dreamshops.exceptions.ResourceNotFoundException;
import com.nicolas.dreamshops.model.Cart;
import com.nicolas.dreamshops.model.CartItem;
import com.nicolas.dreamshops.repository.CartRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.Set;

@Service
@RequiredArgsConstructor
public class CartService implements ICartService {
    private final CartRepository cartRepository;

    @Override
    public Cart getCart(Long id) {
        Cart cart = cartRepository.findById(id).orElseThrow(() -> new ResourceNotFoundException("Cart not found"));
        return cart;
    }

    @Override
    public void clearCart(Long id) {
        cartRepository
                .findById(id)
                .ifPresentOrElse(cartRepository::delete, () -> {
                    throw new ResourceNotFoundException("Cart not found");
                });
    }

    @Override
    public BigDecimal getTotalPrice(Long id) {
        Cart cart = cartRepository.findById(id).orElseThrow(() -> new ResourceNotFoundException("Cart not found"));
        return cart.getTotalPrice();
    }
}
