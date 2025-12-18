package com.dailycodework.agroshop.controller.dto.payments;

import java.math.BigDecimal;

public record ItemDTO(
    Long id,
    String title,
    Integer quantity,
    BigDecimal unitPrice
) {}
