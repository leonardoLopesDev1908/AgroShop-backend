package com.dailycodework.agroshop.controller.dto.register;

import jakarta.validation.constraints.NotBlank;

public record AddressRegisterDTO(
                @NotBlank(message="Campo obrigatório")
                String street,
                @NotBlank(message="Campo obrigatório")
                String neighborhood,
                @NotBlank(message="Campo obrigatório")
                String number,
                @NotBlank(message="Campo obrigatório")
                String complement,
                @NotBlank(message="Campo obrigatório")
                String city,
                @NotBlank(message="Campo obrigatório")
                String state,
                @NotBlank(message="Campo obrigatório")
                String zipcode
) {}
