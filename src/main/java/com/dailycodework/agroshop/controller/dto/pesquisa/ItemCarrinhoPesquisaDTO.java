package com.dailycodework.agroshop.controller.dto.pesquisa;

import java.math.BigDecimal;

public record ItemCarrinhoPesquisaDTO(
                    ProdutoPesquisaDTO produto,
                    Integer quantidade,
                    BigDecimal precoUnitario
) {}
