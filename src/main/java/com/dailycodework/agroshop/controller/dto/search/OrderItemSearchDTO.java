package com.dailycodework.agroshop.controller.dto.search;

public record OrderItemSearchDTO (
                        Long id,
                        Integer quantidade,
                        ProductSearchDTO product
) {}
