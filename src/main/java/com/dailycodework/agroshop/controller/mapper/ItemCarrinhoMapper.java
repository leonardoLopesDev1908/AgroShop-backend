package com.dailycodework.agroshop.controller.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.NullValuePropertyMappingStrategy;

import com.dailycodework.agroshop.controller.dto.pesquisa.ItemCarrinhoPesquisaDTO;
import com.dailycodework.agroshop.model.ItemCarrinho;

@Mapper(componentModel = "spring", 
        nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
public interface ItemCarrinhoMapper {
    
    ItemCarrinhoPesquisaDTO toDTO(ItemCarrinho item);

}
