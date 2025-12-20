package com.dailycodework.agroshop.controller.dto.register;

import com.dailycodework.agroshop.controller.dto.search.AddressSearchDTO;
import com.dailycodework.agroshop.controller.dto.search.FreteDTO;

public record NewOrderRequest (
    FreteDTO frete,
    AddressSearchDTO endereco
){}
