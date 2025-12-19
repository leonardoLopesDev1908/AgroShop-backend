package com.dailycodework.agroshop.controller.dto.update;

public record UserUpdateDTO (
            String nome, 
            String sobrenome,
            String telefone,
            String email,
            String senhaAtual
){}
