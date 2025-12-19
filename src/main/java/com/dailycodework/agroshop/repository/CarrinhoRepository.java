package com.dailycodework.agroshop.repository;

import java.util.UUID;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.dailycodework.agroshop.model.Cart;

@Repository
public interface CarrinhoRepository extends JpaRepository<Cart, Long>{
    Cart findByUsuarioEmail(String email);
    Cart findByUsuarioId(UUID id);
}
