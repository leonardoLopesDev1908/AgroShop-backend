package com.dailycodework.agroshop.service.Pedido;

import java.util.List;
import java.util.UUID;

import com.dailycodework.agroshop.controller.dto.pesquisa.PedidoPesquisaDTO;

public interface IPedidoService {

    PedidoPesquisaDTO fazerPedido(UUID usuarioId);
    List<PedidoPesquisaDTO> pedidosUsuario(UUID usuarioId);

}
