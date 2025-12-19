package com.dailycodework.agroshop.repository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

import org.springframework.data.jpa.repository.JpaRepository;

import com.dailycodework.agroshop.model.Address;
import com.dailycodework.agroshop.model.User;

public interface EnderecoRepository extends JpaRepository<Address, UUID> {
    
    List<Address> getEnderecoByUsuario(User usuario);
    Optional<Address> findByCepAndNumeroAndComplemento(String cep, 
                                                        String numero, 
                                                        String complemento);
    Optional<Address> findByCepAndNumero(String cep, String numero);
}
