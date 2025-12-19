package com.dailycodework.agroshop.controller.dto.register;

import jakarta.validation.constraints.NotBlank;

public record CategoryRegisterDTO (
                @NotBlank(message = "Campo obrigatório")
                String nome
){}
