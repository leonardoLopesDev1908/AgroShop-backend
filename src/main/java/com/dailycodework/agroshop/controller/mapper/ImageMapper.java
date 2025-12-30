package com.dailycodework.agroshop.controller.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.NullValuePropertyMappingStrategy;

import com.dailycodework.agroshop.controller.dto.register.ImageRegisterDTO;
import com.dailycodework.agroshop.controller.dto.search.ImageSearchDTO;
import com.dailycodework.agroshop.model.Image;

@Mapper(componentModel = "spring", nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
public interface ImageMapper {
    
    @Mapping(target="id", ignore=true)
    @Mapping(target="product", ignore=true)
    Image toEntity(ImageRegisterDTO dto);

    ImageSearchDTO toDTO(Image imagem);
}
