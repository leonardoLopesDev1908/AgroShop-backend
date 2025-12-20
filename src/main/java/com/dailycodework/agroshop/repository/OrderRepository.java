package com.dailycodework.agroshop.repository;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.dailycodework.agroshop.model.Order;

@Repository
public interface OrderRepository extends JpaRepository<Order, Long>, JpaSpecificationExecutor<Order>{
    List<Order> findByUserId(UUID id);
    
    @Query("""
        SELECT SUM(p.valorTotal) FROM Order p 
        WHERE p.data BETWEEN :inicio AND :fim
    """)
    BigDecimal totalVendas(@Param("inicio") LocalDateTime inicio, 
                           @Param("fim") LocalDateTime fim);

    @Query("""
            SELECT YEAR(p.data), MONTH(p.data), SUM(p.valorTotal) 
            FROM Order p 
            WHERE YEAR(p.data) = :ano
            GROUP BY YEAR(p.data), MONTH(p.data)
            ORDER BY YEAR(p.data), MONTH(p.data)    
    """)
    List<Object[]> vendasPorMes(@Param("ano") Integer ano); 

                                      
     @Query(""" 
             SELECT it.product.nome, SUM(it.quantidade) FROM Order p 
             JOIN OrderItem it 
             ON it.order.id = p.id 
             JOIN Product pr 
             ON it.product.id = pr.id 
             WHERE p.data BETWEEN :inicio AND :fim 
             GROUP BY it.product.nome 
             ORDER BY SUM(it.quantidade) 
     """) 
     List<Object[]> produtosMaisVendidos(@Param("inicio") LocalDateTime inicio, @Param("fim") LocalDateTime fim);

}