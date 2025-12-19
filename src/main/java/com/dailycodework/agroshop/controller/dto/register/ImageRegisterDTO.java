package com.dailycodework.agroshop.controller.dto.register;

import java.sql.Blob;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

public record ImageRegisterDTO(
                @NotBlank(message="Campo obrigatório")
                String arquivoNome,
                @NotBlank(message="Campo obrigatório")
                String arquivoTipo,
                @NotNull(message="Campo obrigatório")
                Blob image,
                @NotNull(message="Campo obrigatório")
                String downloadUrl
) {}
