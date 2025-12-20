package com.dailycodework.agroshop.service.Frete;

import java.io.IOException;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.time.Duration;
import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;
import static java.util.stream.Collectors.toList;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import com.dailycodework.agroshop.controller.dto.search.FreteDTO;
import com.dailycodework.agroshop.model.Cart;
import com.dailycodework.agroshop.model.CartItem;
import com.dailycodework.agroshop.model.Product;
import com.dailycodework.agroshop.model.User;
import com.dailycodework.agroshop.service.Product.ProductService;
import com.dailycodework.agroshop.service.User.UserService;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ArrayNode;
import com.fasterxml.jackson.databind.node.ObjectNode;

import jakarta.annotation.PostConstruct;
import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class FreteService implements IFreteService{
    
    @Value("${cep-origem}")
    private String cepOrigem;

    @Value("${melhorenvio.token}")
    private String token;

    private final ProductService serviceProduto;
    private final UserService serviceUsuario;
    private final ObjectMapper objectMapper;

    private HttpClient httpClient; 
    
    @PostConstruct
    public void init() {
        this.httpClient = HttpClient.newBuilder()
            .connectTimeout(Duration.ofSeconds(30))
            .build();
    }

    @Override
    public List<FreteDTO> freteProduto(Long idProduto, String cepDestino) throws IOException, InterruptedException{
        Product produto = serviceProduto.buscarPorId(idProduto);

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
                .sorted(Comparator.comparing(FreteDTO::getPrice))
                .collect(Collectors.toList());

            return fretes.subList(0, 3);
        } catch (IOException e) {
            System.out.println(e.getMessage());
        }

        return null;
    }

    @Override
    public List<FreteDTO> freteItensCarrinho(String cepDestino) {
        User user = serviceUsuario.getAuthenticatedUsuario();
        Cart carrinho = user.getCarrinho();
        
        try{
            String requestBody = criarRequestBody(cepDestino, carrinho);

            HttpRequest request = criarRequest(requestBody);

            HttpResponse<String> response = httpClient.send(request, HttpResponse.BodyHandlers.ofString());

            List<FreteDTO> fretes = objectMapper.readValue(response.body(), 
                        objectMapper.getTypeFactory().constructCollectionType(List.class, FreteDTO.class));
            
            fretes = fretes.stream()  
                    .filter(frete -> frete.price != null)
                    .sorted(Comparator.comparing(FreteDTO::getPrice))
                    .collect(toList());
                    
            return fretes.subList(0, 3);
            
        }catch(IOException | InterruptedException e){
            System.out.println(e.getMessage());
        }
        return null;
    }

    private String criarRequestBody(String cepDestino, Cart carrinho) throws IOException{
        ObjectNode requestBody = objectMapper.createObjectNode();

        ObjectNode fromNode = requestBody.putObject("from");
        fromNode.put("postal_code", cepOrigem);
        
        ObjectNode toNode = requestBody.putObject("to");
        toNode.put("postal_code", cepDestino);

        ArrayNode productsArray = requestBody.putArray("products");
        
        for(CartItem item : carrinho.getItems()){
            ObjectNode productNode = objectMapper.createObjectNode();
            Product produto = item.getProduct();

            productNode.put("id", produto.getId());
            productNode.put("weight", produto.getPeso());
            productNode.put("height", produto.getAltura());
            productNode.put("width", produto.getLargura());
            productNode.put("length", produto.getComprimento());
            productNode.put("insurance_value", produto.getPreco());
            productNode.put("quantity", item.getQuantidade());

            productsArray.add(productNode);
        }

        return objectMapper.writeValueAsString(requestBody);
    }

    private HttpRequest criarRequest(String requestBody){
        return HttpRequest.newBuilder()
            .uri(URI.create("https://www.melhorenvio.com.br/api/v2/me/shipment/calculate"))
            .header("Accept", "application/json")
            .header("Content-Type", "application/json")
            .header("Authorization", "Bearer "+ token)
            .header("User-Agent", "Aplicação leonardosilvalls1908@gmail.com")
            .timeout(Duration.ofSeconds(30))
            .POST(HttpRequest.BodyPublishers.ofString(requestBody))
            .build();
    }

}
