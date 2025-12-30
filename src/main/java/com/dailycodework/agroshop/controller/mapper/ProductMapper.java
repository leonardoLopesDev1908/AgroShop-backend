package com.dailycodework.agroshop.controller.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;
import org.mapstruct.NullValuePropertyMappingStrategy;

import com.dailycodework.agroshop.controller.dto.register.ProductRegisterDTO;
import com.dailycodework.agroshop.controller.dto.search.ProductSearchDTO;
import com.dailycodework.agroshop.controller.dto.update.ProductUpdateDTO;
import com.dailycodework.agroshop.model.Product;

@Mapper(componentModel = "spring", 
        nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
public interface ProductMapper {

    @Mapping(target="id", ignore=true)
    @Mapping(target="avaliacoes", ignore=true)
    @Mapping(target="imagens", ignore=true)
    Product toEntity(ProductRegisterDTO dto);
 
    void updateProdutoFromDto(ProductUpdateDTO dto, @MappingTarget Product produto);

    ProductSearchDTO toDTO(Product produto);
}
