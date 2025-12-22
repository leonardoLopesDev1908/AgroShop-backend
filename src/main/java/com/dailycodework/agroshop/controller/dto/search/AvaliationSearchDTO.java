package com.dailycodework.agroshop.controller.dto.search;

import java.time.LocalDateTime;

public record AvaliationSearchDTO(
                    String codigoPublico,
                    String titulo,
                    Double nota,
                    String comentario,
                    UserSearchDTO user,
                    LocalDateTime data
) {}
