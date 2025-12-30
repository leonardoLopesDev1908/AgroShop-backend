package com.dailycodework.agroshop.controller.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.NullValuePropertyMappingStrategy;

import com.dailycodework.agroshop.controller.dto.register.AvaliationRegisterDTO;
import com.dailycodework.agroshop.controller.dto.search.AvaliationSearchDTO;
import com.dailycodework.agroshop.model.Avaliacao;

@Mapper(componentModel = "spring", 
        nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
public interface AvaliationMapper {
    
    @Mapping(target="id", ignore=true)
    @Mapping(target="data", ignore=true)
    @Mapping(target="codigoPublico", ignore=true)
    @Mapping(target="user", ignore=true)
    @Mapping(target="product", ignore=true)
    Avaliacao toEntity(AvaliationRegisterDTO dto);

    AvaliationSearchDTO toDTO(Avaliacao comentario);
}
