package com.dailycodework.agroshop.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.dailycodework.agroshop.model.CartItem;

@Repository
public interface ItemCarrinhoRepository extends JpaRepository<CartItem, Long>{

    List<CartItem> findByProdutoId(Long id);
    void deleteAllByCarrinhoId(Long id);
    List<CartItem> getAllByCarrinhoId(Long id);
}
