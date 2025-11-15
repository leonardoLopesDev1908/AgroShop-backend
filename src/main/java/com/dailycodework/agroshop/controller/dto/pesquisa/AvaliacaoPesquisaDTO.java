package com.dailycodework.agroshop.controller.dto.pesquisa;

import java.time.LocalDateTime;
import java.util.UUID;

public record AvaliacaoPesquisaDTO(
                    String codigoPublico,
                    String titulo,
                    Double nota,
                    String comentario,
                    UsuarioPesquisaDTO usuario,
                    LocalDateTime data
) {}
