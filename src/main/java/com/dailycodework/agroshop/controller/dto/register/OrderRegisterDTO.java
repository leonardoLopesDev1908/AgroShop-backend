package com.dailycodework.agroshop.controller.dto.register;

import java.math.BigDecimal;
import java.util.Set;

import jakarta.validation.constraints.NotEmpty;

public record  OrderRegisterDTO(
                    @NotEmpty(message = "O pedido deve conter ao menos um item")
                    Set<OrderItemRegisterDTO> itens,
                    BigDecimal frete
) {}
