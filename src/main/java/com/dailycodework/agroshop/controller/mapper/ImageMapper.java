package com.dailycodework.agroshop.controller.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.NullValuePropertyMappingStrategy;

import com.dailycodework.agroshop.controller.dto.register.ImageRegisterDTO;
import com.dailycodework.agroshop.controller.dto.search.ImageSearchDTO;
import com.dailycodework.agroshop.model.Image;

@Mapper(componentModel = "spring", nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
public interface ImageMapper {
    
    Image toEntity(ImageRegisterDTO dto);
    ImageSearchDTO toDTO(Image imagem);
}
