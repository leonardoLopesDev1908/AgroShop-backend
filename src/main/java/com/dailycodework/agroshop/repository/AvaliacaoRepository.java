package com.dailycodework.agroshop.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.dailycodework.agroshop.model.Avaliacao;
import com.dailycodework.agroshop.model.Usuario;
import com.dailycodework.agroshop.model.Produto;

@Repository
public interface AvaliacaoRepository extends JpaRepository<Avaliacao, Long>  {
    
    List<Avaliacao> findAllByProduto(Produto produto);

    void deleteByCodigoPublico(String codigoPublico);
    
    boolean existsByUsuarioAndProduto(Usuario user, Produto produto);
}
