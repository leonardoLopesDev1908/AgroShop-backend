package com.dailycodework.agroshop.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.dailycodework.agroshop.model.OrderItem;

@Repository
public interface ItemPedidoRepository extends JpaRepository<OrderItem, Long>{
    
    List<OrderItem> findByProdutoId(Long id);
}
