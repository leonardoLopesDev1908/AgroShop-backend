package com.dailycodework.agroshop.controller.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.NullValuePropertyMappingStrategy;

import com.dailycodework.agroshop.controller.dto.register.AddressRegisterDTO;
import com.dailycodework.agroshop.controller.dto.search.AddressSearchDTO;
import com.dailycodework.agroshop.model.Address;

@Mapper(componentModel = "spring", nullValuePropertyMappingStrategy=
                                    NullValuePropertyMappingStrategy.IGNORE)
public interface AddressMapper {
    
    @Mapping(target = "id", ignore = true)
    @Mapping(target = "user", ignore = true)
    Address toEntity(AddressRegisterDTO dto);
    
    AddressSearchDTO toDTO(Address endereco);
}
