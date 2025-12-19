package com.dailycodework.agroshop.controller.dto.pesquisa;

import java.time.LocalDateTime;
import java.util.UUID;

public record AvaliationSearchDTO(
                    String codigoPublico,
                    String titulo,
                    Double nota,
                    String comentario,
                    UserSearchDTO usuario,
                    LocalDateTime data
) {}
