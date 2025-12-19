package com.dailycodework.agroshop.controller.dto.pesquisa;

import java.math.BigDecimal;

public record CartItemSearchDTO(
                    ProductSearchDTO produto,
                    Integer quantidade,
                    BigDecimal precoUnitario
) {}
