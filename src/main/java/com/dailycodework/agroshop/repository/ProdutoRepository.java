package com.dailycodework.agroshop.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.dailycodework.agroshop.model.Categoria;
import com.dailycodework.agroshop.model.Produto;

@Repository
public interface ProdutoRepository extends JpaRepository<Produto, Long>, JpaSpecificationExecutor<Produto>{
    
    List<Produto> findByNomeContaining(String nome); 
    List<Produto> findByMarcaContaining(String marca);
    List<Produto> findByCategoria(Categoria categoria);
    
    @Query("SELECT p FROM Produto p WHERE p.categoria <> :categoria OR p.categoria IS NULL")
    List<Produto> findTop10ByCategoriaNotOrCategoriaIsNull(@Param("categoria") Categoria categoria);

    List<Produto> findByMarcaAndNome(String marca, String nome);
    boolean existsByNomeAndMarca(String nome, String marca);
    boolean existsByNomeAndMarcaAndIdNot(String nome, String marca, Long id);
    Integer findEstoqueById(Long id);

    @Query("SELECT COUNT(p) FROM Produto p ORDER BY COUNT(p)")
    Integer totalProdutos();
}
