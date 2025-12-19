package com.dailycodework.agroshop.controller.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.NullValuePropertyMappingStrategy;

import com.dailycodework.agroshop.controller.dto.pesquisa.AddressSearchDTO;
import com.dailycodework.agroshop.controller.dto.register.AddressRegisterDTO;
import com.dailycodework.agroshop.model.Address;

@Mapper(componentModel = "spring", nullValuePropertyMappingStrategy=
                                    NullValuePropertyMappingStrategy.IGNORE)
public interface AddressMapper {
    
    Address toEntity(AddressRegisterDTO dto);
    AddressSearchDTO toDTO(Address endereco);
}
