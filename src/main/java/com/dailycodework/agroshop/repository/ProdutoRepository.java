package com.dailycodework.agroshop.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;

import com.dailycodework.agroshop.model.Produto;

public interface ProdutoRepository extends JpaRepository<Produto, Long>, JpaSpecificationExecutor<Produto>{
    
    List<Produto> findByNomeContaining(String nome); 
    List<Produto> findByMarcaContaining(String marca);
    // List<Produto> findByCategoria(String categoria);
    List<Produto> findByMarcaAndNome(String marca, String nome);
    // List<Produto> findByCategoriaAndMarca(String categoria, String marca);
    // List<Produto> findByCategoriaAndNome(String categoria, String nome);

    boolean existsByNomeAndMarca(String nome, String marca);
    boolean existsByNomeAndMarcaAndIdNot(String nome, String marca, Long id);
    Integer findEstoqueById(Long id);

    @Query("SELECT COUNT(p) FROM Produto p ORDER BY COUNT(p)")
    Integer totalProdutos();
}
