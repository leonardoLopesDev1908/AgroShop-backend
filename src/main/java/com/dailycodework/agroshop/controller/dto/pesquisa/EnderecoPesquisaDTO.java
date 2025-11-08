package com.dailycodework.agroshop.controller.dto.pesquisa;

public record EnderecoPesquisaDTO(  
                    String endereco,
                    String numero,
                    String complemento,
                    String cidade,
                    String estado,
                    String cep
) {}
