package com.dailycodework.agroshop.controller.dto.cadastro;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

public record AvaliacaoCadastroDTO(
                @NotBlank(message="Campo obrigatório")
                String titulo,
                @NotBlank(message = "Campo obrigatório")
                String comentario,
                @NotNull(message = "Campo obrigatório")
                Double nota
) {}
