package com.dailycodework.agroshop.controller.dto.register;

import java.util.List;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

public record UserRegisterDTO (
                @NotBlank(message="Campo obrigatório")
                String nome,
                @NotBlank(message="Campo obrigatório")
                String sobrenome,
                @Email(message="Campo obrigatório")
                String email,
                @NotBlank(message="Campo obrigatório")
                String telefone,
                @NotBlank(message="Campo obrigatório")
                @Size(min = 8, message = "A senha deve ter no mínimo 8 digitos")
                String senha,
                @NotNull
                List<AddressRegisterDTO> endereco,
                String role
){}
