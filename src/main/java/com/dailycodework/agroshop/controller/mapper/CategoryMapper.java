package com.dailycodework.agroshop.controller.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.NullValuePropertyMappingStrategy;

import com.dailycodework.agroshop.controller.dto.register.CategoryRegisterDTO;
import com.dailycodework.agroshop.model.Category;

@Mapper(componentModel = "spring", 
        nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE,
        uses = {ImageMapper.class})
public interface CategoryMapper {
    
    @Mapping(target="id", ignore=true)
    @Mapping(target="products", ignore=true)
    Category toEntity(CategoryRegisterDTO dto);
}
