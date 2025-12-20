package com.dailycodework.agroshop.controller.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.NullValuePropertyMappingStrategy;

import com.dailycodework.agroshop.controller.dto.register.OrderItemRegisterDTO;
import com.dailycodework.agroshop.controller.dto.search.OrderItemSearchDTO;
import com.dailycodework.agroshop.model.OrderItem;

@Mapper(componentModel = "spring", nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
public interface OrderItemMapper {
    
    OrderItem toEntity(OrderItemRegisterDTO dto);

    OrderItemSearchDTO toDTO(OrderItem item);

}
