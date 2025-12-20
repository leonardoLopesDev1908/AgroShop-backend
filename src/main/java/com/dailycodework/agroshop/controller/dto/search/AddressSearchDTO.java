package com.dailycodework.agroshop.controller.dto.search;

public record AddressSearchDTO(  
                    String endereco,
                    String numero,
                    String complemento,
                    String cidade,
                    String estado,
                    String cep
) {}
