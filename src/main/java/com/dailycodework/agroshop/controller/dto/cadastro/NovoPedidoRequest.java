package com.dailycodework.agroshop.controller.dto.cadastro;

import com.dailycodework.agroshop.controller.dto.pesquisa.EnderecoPesquisaDTO;
import com.dailycodework.agroshop.controller.dto.pesquisa.FreteDTO;

public record NovoPedidoRequest (
    FreteDTO frete,
    EnderecoPesquisaDTO endereco
){}
