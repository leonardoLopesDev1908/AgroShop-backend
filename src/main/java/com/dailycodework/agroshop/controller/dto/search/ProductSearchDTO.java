package com.dailycodework.agroshop.controller.dto.search;

import java.math.BigDecimal;
import java.util.List;

import com.dailycodework.agroshop.model.Category;

public record ProductSearchDTO(
            Long id,
            String nome,
            String marca,
            BigDecimal preco,
            int estoque,
            String descricao,
            Category category,
            List<ImageSearchDTO> imagens
) {}
