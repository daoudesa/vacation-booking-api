package com.example.d288.dao;

import com.example.d288.entities.CartItem;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.web.bind.annotation.CrossOrigin;

@CrossOrigin("*")
public interface CartItemRepository extends JpaRepository<CartItem, Long> {
}