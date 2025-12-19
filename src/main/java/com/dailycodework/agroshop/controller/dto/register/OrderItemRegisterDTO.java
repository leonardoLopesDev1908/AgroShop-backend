package com.dailycodework.agroshop.controller.dto.register;

import java.math.BigDecimal;

import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.NotNull;


public record OrderItemRegisterDTO (
                    @NotNull   
                    Long produtoId,
                    @DecimalMin(value="1", message="Quantidade mínima deve ser 1")
                    Integer quantidade,
                    @DecimalMin(value="0.1", message="O preço não pode ser menor ou igual a zero")
                    BigDecimal precoUnitario
){}
