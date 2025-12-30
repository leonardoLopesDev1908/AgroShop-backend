package com.dailycodework.agroshop.controller.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;
import org.mapstruct.NullValuePropertyMappingStrategy;

import com.dailycodework.agroshop.controller.dto.register.UserRegisterDTO;
import com.dailycodework.agroshop.controller.dto.search.UserSearchDTO;
import com.dailycodework.agroshop.controller.dto.update.UserUpdateDTO;
import com.dailycodework.agroshop.model.User;

@Mapper(componentModel = "spring",
        nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
public interface UserMapper {
    
    @Mapping(target="id", ignore=true)
    User toEntity(UserRegisterDTO dto);

    UserSearchDTO toDTO(User usuario);

    void updateUsuarioFromDto(UserUpdateDTO dto, @MappingTarget User produto);
}
