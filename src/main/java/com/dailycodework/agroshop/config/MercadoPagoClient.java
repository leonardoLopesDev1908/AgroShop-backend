package com.dailycodework.agroshop.config;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import com.dailycodework.agroshop.controller.dto.payments.CreatePreferenceRequestDTO;
import com.dailycodework.agroshop.controller.dto.payments.CreatePreferenceResponseDTO;
import com.dailycodework.agroshop.model.Payer;
import com.dailycodework.agroshop.model.PaymentEntity;
import com.dailycodework.agroshop.model.enums.PaymentStatus;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.mercadopago.MercadoPagoConfig;
import com.mercadopago.client.payment.PaymentClient;
import com.mercadopago.client.preference.PreferenceBackUrlsRequest;
import com.mercadopago.client.preference.PreferenceClient;
import com.mercadopago.client.preference.PreferenceItemRequest;
import com.mercadopago.client.preference.PreferencePayerRequest;
import com.mercadopago.client.preference.PreferenceRequest;
import com.mercadopago.exceptions.MPApiException;
import com.mercadopago.exceptions.MPException;
import com.mercadopago.resources.payment.Payment;
import com.mercadopago.resources.preference.Preference;

import jakarta.annotation.PostConstruct;
import lombok.extern.slf4j.Slf4j;

@Component
@Slf4j
public class MercadoPagoClient {

    @Value("${mercadopago.notification-url}")
    private String notificationUrl;

    @Value("${mercadopago.access-token}")
    private String accessToken;

    @PostConstruct
    public void init(){
        MercadoPagoConfig.setAccessToken(accessToken);
        log.info("iniciando mercado pago");

        if(accessToken == null || accessToken.trim().isEmpty()){
            log.error("AccessToken inválido");
        }
    }

    public CreatePreferenceResponseDTO createPreference(CreatePreferenceRequestDTO inputData, String orderNumber) 
                                                throws MPException, MPApiException {
        log.info("Entrou no MercadoPagoClient");
        try{
            PreferenceClient preferenceClient = new PreferenceClient();
            List<PreferenceItemRequest> items;
            items = inputData.items().stream()
                    .map(item -> PreferenceItemRequest.builder()
                            .id(item.id().toString())
                            .title(item.title())
                            .quantity(item.quantity())
                            .unitPrice(item.unitPrice())
                            .build())
                    .collect(Collectors.toList());
    
            PreferencePayerRequest payer = PreferencePayerRequest.builder()
                .name(inputData.payer().name())
                .email(inputData.payer().email())
                .build();
    
            PreferenceBackUrlsRequest backUrlsRequest = PreferenceBackUrlsRequest.builder()
                .success(inputData.backUrls().success())
                .pending(inputData.backUrls().pending())
                .failure(inputData.backUrls().failure())
                .build();
            
            PreferenceRequest preferenceRequest = PreferenceRequest.builder()   
                .items(items)
                .payer(payer)
                .backUrls(backUrlsRequest)
                .notificationUrl(notificationUrl)
                .externalReference(orderNumber)
                .build();
            
            log.info("Criando preferência para orderNumber: {}", orderNumber);
            log.info("Notification URL: {}", notificationUrl);

            log.info("Enviando requisição para API do Mercado Pago...");
            Preference preference = preferenceClient.create(preferenceRequest);

            log.info("Preference criada com sucesso");

            return new CreatePreferenceResponseDTO(
                preference.getId(),
                preference.getInitPoint()
            );

    } catch(MPApiException e){
            log.error("=== ERRO NA API DO MERCADO PAGO ===");
            log.error("Status Code: {}", e.getStatusCode());
            log.error("Mensagem: {}", e.getMessage());
            
            if (e.getApiResponse() != null && e.getApiResponse().getContent() != null) {
                String responseContent = e.getApiResponse().getContent();
                log.error("Resposta da API: {}", responseContent);

                try {
                    ObjectMapper mapper = new ObjectMapper();
                    JsonNode jsonNode = mapper.readTree(responseContent);
                    log.error("Erro detalhado: {}", jsonNode.toPrettyString());
                } catch (Exception jsonEx) {
                    log.error("Conteúdo da resposta (raw): {}", responseContent);
                }
            }
            
            throw e;

        } catch(MPException e){
            log.error("=== ERRO NO SDK DO MERCADO PAGO ===");
            log.error("Mensagem: {}", e.getMessage());
            log.error("StackTrace completo:", e);
            throw e;
            
        } catch(Exception e){
            log.error("=== ERRO INESPERADO ===");
            log.error("Tipo: {}", e.getClass().getName());
            log.error("Mensagem: {}", e.getMessage());
            log.error("StackTrace completo:", e);
            throw new RuntimeException("Erro ao criar preferência", e);
        }      
    }

    public PaymentEntity getPaymentStatus(long id) throws MPException, MPApiException{
        
        PaymentClient paymentClient = new PaymentClient();
        Payment paymentMercadoPago = paymentClient.get(id);
        
        if(paymentMercadoPago == null){
            log.error("Pagamento não encontrado");
            throw new MPException("Pagamento não encontrado");
        }
        
        PaymentStatus status = PaymentStatus.valueOf(paymentMercadoPago.getStatus());
        String paymentMethod = paymentMercadoPago.getPaymentMethodId();

        Payer payer = null;

        if(paymentMercadoPago.getPayer() != null &&
                paymentMercadoPago.getPayer().getIdentification() != null
        ){
            payer = Payer.builder() 
                .email(paymentMercadoPago.getPayer().getEmail())
                .firstName(paymentMercadoPago.getPayer().getFirstName())
                .lastName(paymentMercadoPago.getPayer().getLastName())
                .identification(Payer.Identification.builder()
                    .type(paymentMercadoPago.getPayer().getIdentification().getType())
                    .number(paymentMercadoPago.getPayer().getIdentification().getNumber())
                    .build()
                )
                .build(); 
        }

        return PaymentEntity.builder()
            .id(paymentMercadoPago.getId().toString())
            .orderId(paymentMercadoPago.getExternalReference())
            .status(status)
            .amount(paymentMercadoPago.getTransactionAmount() != null ? 
                    paymentMercadoPago.getTransactionAmount().toString() : null
            )
            .payer(payer)
            .paymentMethodId(paymentMethod)
            .build();
    }
}