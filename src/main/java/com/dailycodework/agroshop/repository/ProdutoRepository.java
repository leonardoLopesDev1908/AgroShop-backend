package com.dailycodework.agroshop.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.dailycodework.agroshop.model.Category;
import com.dailycodework.agroshop.model.Product;

@Repository
public interface ProdutoRepository extends JpaRepository<Product, Long>, JpaSpecificationExecutor<Product>{
    
    List<Product> findByNomeContaining(String nome); 
    List<Product> findByMarcaContaining(String marca);
    List<Product> findByCategoria(Category categoria);
    
    @Query("SELECT p FROM Produto p WHERE p.categoria <> :categoria OR p.categoria IS NULL")
    List<Product> findTop10ByCategoriaNotOrCategoriaIsNull(@Param("categoria") Category categoria);

    List<Product> findByMarcaAndNome(String marca, String nome);
    boolean existsByNomeAndMarca(String nome, String marca);
    boolean existsByNomeAndMarcaAndIdNot(String nome, String marca, Long id);
    Integer findEstoqueById(Long id);

    @Query("SELECT COUNT(p) FROM Produto p ORDER BY COUNT(p)")
    Integer totalProdutos();
}
