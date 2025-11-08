package com.dailycodework.agroshop.repository;

import java.util.List;
import java.util.UUID;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import com.dailycodework.agroshop.model.Usuario;

@Repository
public interface UsuarioRepository extends JpaRepository<Usuario, UUID> {

    boolean existsByEmail(String email);
    List<Usuario> findByNome(String nome);
    Usuario findByEmail(String email);

    @Query("""
            SELECT COUNT(c) FROM Usuario c
            JOIN c.roles r
            WHERE r.nome = 'Cliente'
            """)
    Integer totalClientes();
}
