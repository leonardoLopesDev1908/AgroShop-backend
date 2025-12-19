package com.dailycodework.agroshop.controller.dto.update;

public record UpdatePassword (
            String email,
            String senhaAtual,
            String senhaNova
){}
