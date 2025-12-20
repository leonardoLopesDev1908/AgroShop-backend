package com.dailycodework.agroshop.controller.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.MappingTarget;
import org.mapstruct.NullValuePropertyMappingStrategy;

import com.dailycodework.agroshop.controller.dto.register.ProductRegisterDTO;
import com.dailycodework.agroshop.controller.dto.search.ProductSearchDTO;
import com.dailycodework.agroshop.controller.dto.update.ProductUpdateDTO;
import com.dailycodework.agroshop.model.Product;

@Mapper(componentModel = "spring", 
        nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
public interface ProductMapper {

    Product toEntity(ProductRegisterDTO dto);
 
    void updateProdutoFromDto(ProductUpdateDTO dto, @MappingTarget Product produto);

    ProductSearchDTO toDTO(Product produto);
}
