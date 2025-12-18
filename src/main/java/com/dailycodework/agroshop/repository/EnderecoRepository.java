package com.dailycodework.agroshop.repository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

import org.springframework.data.jpa.repository.JpaRepository;

import com.dailycodework.agroshop.model.Endereco;
import com.dailycodework.agroshop.model.Usuario;

public interface EnderecoRepository extends JpaRepository<Endereco, UUID> {
    
    List<Endereco> getEnderecoByUsuario(Usuario usuario);
    Optional<Endereco> findByCepAndNumeroAndComplemento(String cep, 
                                                        String numero, 
                                                        String complemento);
    Optional<Endereco> findByCepAndNumero(String cep, String numero);
}
