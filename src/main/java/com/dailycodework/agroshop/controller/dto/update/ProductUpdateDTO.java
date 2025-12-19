package com.dailycodework.agroshop.controller.dto.update;

import java.math.BigDecimal;

import com.dailycodework.agroshop.model.Category;

public record ProductUpdateDTO (
                String nome,
                String marca,
                BigDecimal preco,
                int estoque,
                String descricao,
                Category categoria
){}
