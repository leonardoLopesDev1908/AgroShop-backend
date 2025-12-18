package com.dailycodework.agroshop.controller.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.NullValuePropertyMappingStrategy;

import com.dailycodework.agroshop.controller.dto.cadastro.ItemPedidoCadastroDTO;
import com.dailycodework.agroshop.model.ItemPedido;

@Mapper(componentModel = "spring", nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
public interface ItemPedidoMapper {
    
    ItemPedido toEntity(ItemPedidoCadastroDTO dto);

    ItemPedidoCadastroDTO toDTO(ItemPedido item);

}
