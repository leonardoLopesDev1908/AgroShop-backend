package com.dailycodework.agroshop.controller.controllers;

import java.io.IOException;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.util.List;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.dailycodework.agroshop.controller.dto.pesquisa.FreteDTO;
import com.dailycodework.agroshop.model.Produto;
import com.dailycodework.agroshop.response.ApiResponse;
import com.dailycodework.agroshop.service.Produto.ProdutoService;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;

import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("${api.prefix}/melhorenvio")
@RequiredArgsConstructor
public class FreteAuthController {
    
    @Value("${cep-origem}")
    private String cepOrigem;

    @Value("${melhorenvio.token}")
    private String token;

    private final ProdutoService serviceProduto;
    
    @PostMapping("/frete/produto/cotar")
    public ResponseEntity<ApiResponse> calcularFreteProduto(@RequestParam Long idProduto,
                                                            @RequestParam String cepDestino) 
                                                                throws IOException, InterruptedException{
            
            Produto produto = serviceProduto.buscarPorId(idProduto);
            
            var request = HttpRequest.newBuilder()
            .uri(URI.create("https://www.melhorenvio.com.br/api/v2/me/shipment/calculate"))
            .header("Accept", "application/json")
            .header("Content-Type", "application/json")
            .header("Authorization", "Bearer "+ token)
            .header("User-Agent", "Aplicação leonardosilvalls1908@gmail.com")
            .method("POST", HttpRequest.BodyPublishers.ofString(""+
                "{\"from\": "+
                    "{\"postal_code\":\""+ cepOrigem +"\"},"+
                "\"to\":"+
                    "{\"postal_code\":\"" + cepDestino + "\"},"+
                "\"products\":["+
                    "{\"id\":\"" + produto.getId() + "\","+
                    "\"width\":" + produto.getLargura() + ","+
                    "\"height\":" + produto.getAltura() + ","+
                    "\"length\":" + produto.getComprimento() + ","+
                    "\"weight\":"+ produto.getPeso() + ","+
                    "\"insurance_value\":" + produto.getPreco() + ","+
                    "\"quantity\":1}"+
                "]}"))
            .build();

            try {
                HttpResponse<String> response = HttpClient.
                                newHttpClient()
                                .send(request, HttpResponse.BodyHandlers.ofString());
                
                ObjectMapper mapper = new ObjectMapper();

                List<FreteDTO> fretes = mapper.readValue(response.body(),
                        new TypeReference<List<FreteDTO>>() {});

                fretes = fretes.stream()    
                    .filter(frete -> frete.price != null)
                    .collect(Collectors.toList());

                return ResponseEntity.ok(new ApiResponse("Sucesso!", fretes));
            } catch (IOException e) {
                System.out.println(e.getMessage());
            }
            return ResponseEntity.noContent().build();
        }

        // @PostMapping("/frete/itens/cotar")
        // public ResponseEntity<ApiResponse> calcularFreteItens(@RequestParam String accessToken){
    //     //calcular frete para todos os itens no carrinho
    // }
}
