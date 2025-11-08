package com.dailycodework.agroshop.controller.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.NullValuePropertyMappingStrategy;

import com.dailycodework.agroshop.controller.dto.cadastro.EnderecoCadastroDTO;
import com.dailycodework.agroshop.controller.dto.pesquisa.EnderecoPesquisaDTO;
import com.dailycodework.agroshop.model.Endereco;

@Mapper(componentModel = "spring", nullValuePropertyMappingStrategy=
                                    NullValuePropertyMappingStrategy.IGNORE)
public interface EnderecoMapper {
    
    Endereco toEntity(EnderecoCadastroDTO dto);
    EnderecoPesquisaDTO toDTO(Endereco endereco);
}
