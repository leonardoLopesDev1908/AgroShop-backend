package com.dailycodework.agroshop.controller.dto.pesquisa;

import java.math.BigDecimal;
import java.util.List;

import com.dailycodework.agroshop.model.Categoria;

public record ProdutoPesquisaDTO(
            Long id,
            String nome,
            String marca,
            BigDecimal preco,
            int estoque,
            String descricao,
            Categoria categoria,
            List<ImagemPesquisaDTO> imagens
) {}
