package com.nicolas.dreamshops.repository;

import com.nicolas.dreamshops.model.Cart;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CartRepository extends JpaRepository<Cart, Long> {
}
