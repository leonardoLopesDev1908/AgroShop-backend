package com.dailycodework.agroshop.controller.dto.cadastro;

import jakarta.validation.constraints.NotBlank;

public record EnderecoCadastroDTO(
                @NotBlank(message="Campo obrigatório")
                String endereco,
                @NotBlank(message="Campo obrigatório")
                String numero,
                @NotBlank(message="Campo obrigatório")
                String complemento,
                @NotBlank(message="Campo obrigatório")
                String cidade,
                @NotBlank(message="Campo obrigatório")
                String estado,
                @NotBlank(message="Campo obrigatório")
                String cep
) {}
