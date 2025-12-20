package com.dailycodework.agroshop.controller.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.NullValuePropertyMappingStrategy;

import com.dailycodework.agroshop.controller.dto.register.AvaliationRegisterDTO;
import com.dailycodework.agroshop.controller.dto.search.AvaliationSearchDTO;
import com.dailycodework.agroshop.model.Avaliacao;

@Mapper(componentModel = "spring", 
        nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
public interface AvaliationMapper {
    
    Avaliacao toEntity(AvaliationRegisterDTO dto);

    AvaliationSearchDTO toDTO(Avaliacao comentario);
}
