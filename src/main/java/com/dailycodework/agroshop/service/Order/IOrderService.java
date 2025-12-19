package com.dailycodework.agroshop.service.Order;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;
import java.util.UUID;

import org.springframework.data.domain.Page;

import com.dailycodework.agroshop.controller.dto.pesquisa.AddressSearchDTO;
import com.dailycodework.agroshop.controller.dto.pesquisa.CompleteOrderDTO;
import com.dailycodework.agroshop.controller.dto.pesquisa.OrderSearchDTO;
import com.dailycodework.agroshop.model.Order;
import com.dailycodework.agroshop.model.User;

public interface IOrderService {

    OrderSearchDTO fazerPedido(User usuario, BigDecimal frete, 
                                    AddressSearchDTO endereco);
    List<OrderSearchDTO> pedidosUsuario(UUID usuarioId);
    Page<Order> searchPedidos(Long id, String email, LocalDate dataInicio,     
                               LocalDate dataFim, Integer pagina);
    Order buscaPedidoPorId(Long id);
    CompleteOrderDTO getPedidoCompleto(Long id);
    OrderSearchDTO atualizarPedido(Long id, String novoStatus);
    OrderSearchDTO pedidoCancelar(Long id);
    void excluirPedido(Long id);                               
}
