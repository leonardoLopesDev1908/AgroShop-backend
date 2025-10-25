package com.dailycodework.agroshop.controller.dto.pesquisa;

import java.time.LocalDateTime;
import java.util.Set;

import com.dailycodework.agroshop.controller.dto.cadastro.ItemPedidoCadastroDTO;
import com.dailycodework.agroshop.model.enums.PedidoStatus;

public record PedidoPesquisaDTO(
                    Long id,
                    String data,
                    Set<ItemPedidoPesquisaDTO> itens,
                    PedidoStatus status
) {}
