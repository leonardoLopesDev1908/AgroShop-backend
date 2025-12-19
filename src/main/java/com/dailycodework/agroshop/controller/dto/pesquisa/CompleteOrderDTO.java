package com.dailycodework.agroshop.controller.dto.pesquisa;

public record CompleteOrderDTO(
        OrderSearchDTO pedido,
        AddressSearchDTO endereco
) {}
