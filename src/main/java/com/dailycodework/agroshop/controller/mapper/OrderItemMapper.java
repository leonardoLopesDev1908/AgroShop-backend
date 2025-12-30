package com.dailycodework.agroshop.controller.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.NullValuePropertyMappingStrategy;

import com.dailycodework.agroshop.controller.dto.register.OrderItemRegisterDTO;
import com.dailycodework.agroshop.controller.dto.search.OrderItemSearchDTO;
import com.dailycodework.agroshop.model.OrderItem;

@Mapper(componentModel = "spring", nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
public interface OrderItemMapper {
    
    @Mapping(target="id", ignore=true)
    @Mapping(target="order", ignore=true)
    @Mapping(target="product", ignore=true)
    @Mapping(target="preco", ignore=true)
    OrderItem toEntity(OrderItemRegisterDTO dto);

    OrderItemSearchDTO toDTO(OrderItem item);

}
