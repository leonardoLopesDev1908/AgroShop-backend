package com.dailycodework.agroshop.repository;

import java.util.UUID;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.dailycodework.agroshop.model.Cart;

@Repository
public interface CartRepository extends JpaRepository<Cart, Long>{
    Cart findByUserEmail(String email);
    Cart findByUserId(UUID id);
}
