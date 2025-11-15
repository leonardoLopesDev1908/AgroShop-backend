package com.dailycodework.agroshop.controller.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.NullValuePropertyMappingStrategy;

import com.dailycodework.agroshop.controller.dto.cadastro.AvaliacaoCadastroDTO;
import com.dailycodework.agroshop.controller.dto.pesquisa.AvaliacaoPesquisaDTO;
import com.dailycodework.agroshop.model.Avaliacao;

@Mapper(componentModel = "spring", 
        nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
public interface AvaliacaoMapper {
    
    Avaliacao toEntity(AvaliacaoCadastroDTO dto);

    AvaliacaoPesquisaDTO toDTO(Avaliacao comentario);
}
