package com.dailycodework.agroshop.controller.controllers;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.dailycodework.agroshop.controller.dto.payments.MercadoPagoConfigDTO;
import com.dailycodework.agroshop.response.ApiResponse;
import com.dailycodework.agroshop.service.Payment.PaymentNotificationService;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@RestController
@RequestMapping("${api.prefix}/webhooks")
@Slf4j
@RequiredArgsConstructor
public class WebHookController {

    private final PaymentNotificationService proccessPaymentNotification;
    
    @PostMapping("/mercadopago")
    public ResponseEntity<ApiResponse> handleNotification(@RequestBody MercadoPagoConfigDTO dto){
        
        String resourceId = dto.getData().getId();
        String resourceType = dto.getType();

        if(!resourceType.equals("payment")){
            log.info("Invalid payment type: " + resourceType);
            return ResponseEntity.ok().build();
        }

        try{
            var result = proccessPaymentNotification.proccessNotification(resourceId, resourceType);
        
            log.info("Webhook processed successfully for resource ID: {}, and type: {}",    
                                                result.isSuccess(), result.getUpdatedStatus());

        } catch(Exception e){
            log.error("Erro processando webhook");
        }
        return ResponseEntity.ok().body(new ApiResponse("Sucesso!", null));
    }
}
