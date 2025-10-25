package com.dailycodework.agroshop.controller.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.NullValuePropertyMappingStrategy;

import com.dailycodework.agroshop.controller.dto.cadastro.PedidoCadastroDTO;
import com.dailycodework.agroshop.controller.dto.pesquisa.PedidoPesquisaDTO;
import com.dailycodework.agroshop.model.Pedido;

@Mapper(componentModel = "spring", nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
public interface PedidoMapper {

    Pedido toEntity(PedidoCadastroDTO dto);
    PedidoPesquisaDTO toDTO(Pedido pedido);
}
