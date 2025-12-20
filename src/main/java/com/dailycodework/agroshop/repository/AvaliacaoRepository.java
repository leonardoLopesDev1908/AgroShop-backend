package com.dailycodework.agroshop.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.dailycodework.agroshop.model.Avaliacao;
import com.dailycodework.agroshop.model.Product;
import com.dailycodework.agroshop.model.User;

@Repository
public interface AvaliacaoRepository extends JpaRepository<Avaliacao, Long>  {
    
    List<Avaliacao> findAllByProduct(Product produto);

    void deleteByCodigoPublico(String codigoPublico);
    
    boolean existsByUserAndProduct(User user, Product produto);
}
