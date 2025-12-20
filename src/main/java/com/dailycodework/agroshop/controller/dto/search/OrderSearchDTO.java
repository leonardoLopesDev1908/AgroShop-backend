package com.dailycodework.agroshop.controller.dto.search;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.Set;

import com.dailycodework.agroshop.model.enums.PedidoStatus;

public record OrderSearchDTO(
                    Long id,
                    LocalDateTime data,
                    Set<OrderItemSearchDTO> itens,
                    PedidoStatus status,
                    UserSearchDTO usuario,
                    BigDecimal frete
) {}
