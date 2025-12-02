package com.dailycodework.agroshop.service.Pedido;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;
import java.util.UUID;

import org.springframework.data.domain.Page;

import com.dailycodework.agroshop.controller.dto.pesquisa.PedidoPesquisaDTO;
import com.dailycodework.agroshop.model.Pedido;

public interface IPedidoService {

    PedidoPesquisaDTO fazerPedido(UUID usuarioId, BigDecimal frete);
    List<PedidoPesquisaDTO> pedidosUsuario(UUID usuarioId);
    Page<Pedido> searchPedidos(Long id, String email, LocalDate dataInicio,     
                               LocalDate dataFim, Integer pagina);
    PedidoPesquisaDTO buscaPedidoPorId(Long id);
    PedidoPesquisaDTO atualizarPedido(Long id, String novoStatus);
    PedidoPesquisaDTO pedidoCancelar(Long id);
    void excluirPedido(Long id);                               
}
