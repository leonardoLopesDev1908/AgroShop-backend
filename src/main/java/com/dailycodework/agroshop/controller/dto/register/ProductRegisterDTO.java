package com.dailycodework.agroshop.controller.dto.register;

import java.math.BigDecimal;

import com.dailycodework.agroshop.model.Category;

import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

public record ProductRegisterDTO(
                @NotBlank(message="Campo obrigatório")
                String nome,
                @NotBlank(message="Campo obrigatório")
                String marca,
                @NotNull(message="Campo obrigatório")
                @DecimalMin(value = "0.0", inclusive = false, message = "Preço deve ser maior que 0.0")
                BigDecimal preco,
                @NotNull(message="Campo obrigatório")
                @DecimalMin(value = "0", inclusive = false, message = "Estoque deve ser maior que 0.0")
                int estoque,
                @NotBlank(message="Campo obrigatório")
                String descricao,
                @NotNull(message="Campo obrigatório")
                Category category,
                @NotNull(message="Campo obrigatório")
                BigDecimal peso,
                @NotNull(message="Campo obrigatório")
                BigDecimal altura,
                @NotNull(message="Campo obrigatório")
                BigDecimal largura,
                @NotNull(message="Campo obrigatório")
                BigDecimal comprimento
) {}
