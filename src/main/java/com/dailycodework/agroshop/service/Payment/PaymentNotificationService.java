package com.dailycodework.agroshop.service.Payment;

import org.springframework.stereotype.Service;

import com.dailycodework.agroshop.config.MercadoPagoClient;
import com.dailycodework.agroshop.controller.dto.payments.ProccessNotificationResponseDTO;
import com.dailycodework.agroshop.model.PaymentEntity;
import com.dailycodework.agroshop.repository.PaymentRepository;
import com.dailycodework.agroshop.service.Order.OrderService;
import com.mercadopago.exceptions.MPApiException;
import com.mercadopago.exceptions.MPException;

import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Service
@Slf4j
@RequiredArgsConstructor
public class PaymentNotificationService {
    
    private final MercadoPagoClient mercadoPagoClient;
    private final PaymentRepository repository;
    private final OrderService pedidoService;

    public ProccessNotificationResponseDTO proccessNotification(String id, String type){
        try{
            PaymentEntity payment = mercadoPagoClient.getPaymentStatus(Long.parseLong(id));

            if(paymentAlreadyExists(payment)){
                PaymentEntity paymentSaved = repository.findById(payment.getId())
                    .orElseThrow(() -> new EntityNotFoundException(
                        "Pagamento não encontrado"
                    ));

                String pedidoId = payment.getOrderId();
                
                if(pedidoId != null){
                    updateOrderStatus(payment.getStatus().toString(), Long.valueOf(pedidoId));
                }
                if(!paymentSaved.getStatus().equals(payment.getStatus())){
                    paymentSaved.setStatus(payment.getStatus());
                    repository.save(paymentSaved);
                    return new ProccessNotificationResponseDTO(false, 
                                payment.getStatus().toString());
                }
                return new ProccessNotificationResponseDTO(false, 
                                "PAYMENT ALREADY EXISTS");
            }
            repository.save(payment);

            return new ProccessNotificationResponseDTO(false, 
                payment.getStatus().toString());
        } catch(MPApiException | MPException | NumberFormatException e){
            log.error("Error processing notification: " + e.getMessage());
            return new ProccessNotificationResponseDTO(false, 
                "SERVER_ERROR");
        }
    }

    @SuppressWarnings("empty-statement")
    public void updateOrderStatus(String status, Long orderId){
        switch(status){
            case "APPROVED"-> {
                pedidoService.atualizarPedido(orderId, "CONFIRMADO");
                break;
            }
            case "REJECTED" -> {
                pedidoService.atualizarPedido(orderId, "REJEITADO");
                break;
            } 
            case "PENDING" -> {
                pedidoService.atualizarPedido(orderId, "PENDENTE");
                break;
            }
            case "CANCELLED" -> {
                pedidoService.pedidoCancelar(orderId);
                break;
            } 
        };
    }

    public boolean paymentAlreadyExists(PaymentEntity payment){
        return repository.existsById(payment.getId());
    }
}
