package com.dailycodework.agroshop.controller.controllers;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.dailycodework.agroshop.controller.dto.payments.CreatePreferenceResponseDTO;
import com.dailycodework.agroshop.response.ApiResponse;
import com.dailycodework.agroshop.service.Pagamento.PagamentoService;
import com.mercadopago.exceptions.MPApiException;
import com.mercadopago.exceptions.MPException;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@RestController
@RequestMapping("${api.prefix}/payment")
@RequiredArgsConstructor
@Slf4j
public class PagamentoController {
    
    private final PagamentoService service;

    @PostMapping("/preference/{id}")
    public ResponseEntity<ApiResponse> criarPreference(@PathVariable("id") Long pedidoId) 
                                                            throws MPException, MPApiException{

        log.info("Recebida requisição para criar preferência para pedido: {}", pedidoId);
        try{

            CreatePreferenceResponseDTO response = service.createPreference(pedidoId);

            if(response == null){
                log.error("Response is null");
                return ResponseEntity.badRequest()
                    .body(new ApiResponse("Bad request", null));
            }   

            return ResponseEntity.ok(new ApiResponse("Sucesso!", 
                new CreatePreferenceResponseDTO(
                    response.preferenceId(),
                    response.redirectUrl()
                ))
            );
        } catch(Exception e){
            return ResponseEntity.internalServerError().build();
        }
   }

}
