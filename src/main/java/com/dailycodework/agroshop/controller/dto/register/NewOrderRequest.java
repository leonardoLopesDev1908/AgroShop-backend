package com.dailycodework.agroshop.controller.dto.register;

import com.dailycodework.agroshop.controller.dto.pesquisa.AddressSearchDTO;
import com.dailycodework.agroshop.controller.dto.pesquisa.FreteDTO;

public record NewOrderRequest (
    FreteDTO frete,
    AddressSearchDTO endereco
){}
