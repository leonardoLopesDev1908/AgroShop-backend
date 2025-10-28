package com.dailycodework.agroshop.repository;

import java.util.List;
import java.util.UUID;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

import com.dailycodework.agroshop.model.Pedido;

public interface PedidoRepository extends JpaRepository<Pedido, Long>, JpaSpecificationExecutor<Pedido>{
    List<Pedido> findByUsuarioId(UUID id);
}
