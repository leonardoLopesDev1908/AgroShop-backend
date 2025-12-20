package com.dailycodework.agroshop.controller.dto.search;

import java.math.BigDecimal;

public record CartItemSearchDTO(
                    ProductSearchDTO product,
                    Integer quantidade,
                    BigDecimal precoUnitario
) {}
