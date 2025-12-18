package com.dailycodework.agroshop.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.dailycodework.agroshop.model.ItemCarrinho;

@Repository
public interface ItemCarrinhoRepository extends JpaRepository<ItemCarrinho, Long>{

    List<ItemCarrinho> findByProdutoId(Long id);
    void deleteAllByCarrinhoId(Long id);
    List<ItemCarrinho> getAllByCarrinhoId(Long id);
}
