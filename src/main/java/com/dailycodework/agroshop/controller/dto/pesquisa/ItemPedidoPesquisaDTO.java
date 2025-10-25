package com.dailycodework.agroshop.controller.dto.pesquisa;

public record ItemPedidoPesquisaDTO (
                        Long id,
                        Integer quantidade,
                        ProdutoPesquisaDTO produto
) {}
