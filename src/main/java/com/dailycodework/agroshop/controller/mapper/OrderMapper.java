package com.dailycodework.agroshop.controller.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.NullValuePropertyMappingStrategy;

import com.dailycodework.agroshop.controller.dto.payments.CreatePreferenceRequestDTO;
import com.dailycodework.agroshop.controller.dto.pesquisa.OrderSearchDTO;
import com.dailycodework.agroshop.controller.dto.register.OrderRegisterDTO;
import com.dailycodework.agroshop.model.Order;

@Mapper(componentModel = "spring", nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
public interface OrderMapper {

    Order toEntity(OrderRegisterDTO dto);
    OrderSearchDTO toDTO(Order pedido);
}
