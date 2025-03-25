package com.nicolas.dreamshops.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.math.BigDecimal;
import java.util.Set;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Entity
public class Cart {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @OneToMany(mappedBy = "cart", cascade = CascadeType.ALL, orphanRemoval = true)
    private Set<CartItem> cartItems;

    @Transient
    private BigDecimal totalPrice;

    public void addItem(CartItem item) {
        this.cartItems.add(item);
        item.setCart(this);
        updateTotalPrice();
    }

    public void removeItem(CartItem item) {
        this.cartItems.remove(item);
        item.setCart(null);
        updateTotalPrice();
    }

    public void updateTotalPrice() {
        BigDecimal sum = this.cartItems
                .stream()
                .reduce(
                        BigDecimal.ZERO,
                        (curr, item) -> curr.add(item.getTotalPrice()),
                        BigDecimal::add
                );

        this.totalPrice = sum;
    }
}
