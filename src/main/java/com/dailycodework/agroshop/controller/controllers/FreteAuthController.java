package com.dailycodework.agroshop.controller.controllers;

import java.io.IOException;
import java.util.List;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.dailycodework.agroshop.controller.dto.search.FreteDTO;
import com.dailycodework.agroshop.response.ApiResponse;
import com.dailycodework.agroshop.service.Frete.FreteService;

import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("${api.prefix}")
@RequiredArgsConstructor
public class FreteAuthController {
    
    @Value("${cep-origem}")
    private String cepOrigem;

    @Value("${melhorenvio.token}")
    private String token;

    private final FreteService service;
    
    @PostMapping("/produto/frete/cotacao")
    public ResponseEntity<ApiResponse> calcularFreteProduto(@RequestParam Long idProduto,
                                                            @RequestParam String cepDestino) 
                                                                throws IOException, InterruptedException{
            List<FreteDTO> fretes = service.freteProduto(idProduto, cepDestino);
            return ResponseEntity.ok(new ApiResponse("Sucesso!", fretes));
        }

    @PostMapping("/itens/frete/cotacao")
    public ResponseEntity<ApiResponse> calcularFreteItens(@RequestParam String cepDestino){
        List<FreteDTO> fretes = service.freteItensCarrinho(cepDestino);
        return ResponseEntity.ok(new ApiResponse("Sucesso!", fretes));
    }
}
