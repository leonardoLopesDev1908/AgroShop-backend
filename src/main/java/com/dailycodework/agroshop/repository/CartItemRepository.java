package com.dailycodework.agroshop.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.dailycodework.agroshop.model.CartItem;

@Repository
public interface CartItemRepository extends JpaRepository<CartItem, Long>{

    List<CartItem> findByProductId(Long id);
    void deleteAllByCartId(Long id);
    List<CartItem> getAllByCartId(Long id);
}
