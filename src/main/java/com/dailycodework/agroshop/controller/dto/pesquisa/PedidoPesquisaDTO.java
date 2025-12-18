package com.dailycodework.agroshop.controller.dto.pesquisa;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.Set;

import com.dailycodework.agroshop.model.enums.PedidoStatus;

public record PedidoPesquisaDTO(
                    Long id,
                    LocalDateTime data,
                    Set<ItemPedidoPesquisaDTO> itens,
                    PedidoStatus status,
                    UsuarioPesquisaDTO usuario,
                    BigDecimal frete,
                    EnderecoPesquisaDTO endereco
) {}
