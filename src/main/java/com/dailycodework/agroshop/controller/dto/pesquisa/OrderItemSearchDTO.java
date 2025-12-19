package com.dailycodework.agroshop.controller.dto.pesquisa;

public record OrderItemSearchDTO (
                        Long id,
                        Integer quantidade,
                        ProductSearchDTO produto
) {}
