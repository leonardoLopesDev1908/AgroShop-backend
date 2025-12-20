package com.dailycodework.agroshop.controller.dto.search;

public record CompleteOrderDTO(
        OrderSearchDTO pedido,
        AddressSearchDTO endereco
) {}
