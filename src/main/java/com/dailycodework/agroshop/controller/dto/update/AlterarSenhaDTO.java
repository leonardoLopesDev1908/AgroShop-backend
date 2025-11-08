package com.dailycodework.agroshop.controller.dto.update;

public record AlterarSenhaDTO (
            String email,
            String senhaAtual,
            String senhaNova
){}
