package com.dailycodework.agroshop.repository;

import java.util.List;
import java.util.UUID;

import org.springframework.data.jpa.repository.JpaRepository;

import com.dailycodework.agroshop.model.Endereco;
import com.dailycodework.agroshop.model.Usuario;

public interface EnderecoRepository extends JpaRepository<Endereco, UUID> {
    
    List<Endereco> getEnderecoByUsuario(Usuario usuario);

}
