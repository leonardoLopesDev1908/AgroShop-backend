package com.dailycodework.agroshop.service.Payment;


import java.util.List;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import com.dailycodework.agroshop.config.MercadoPagoClient;
import com.dailycodework.agroshop.controller.dto.payments.CreatePreferenceRequestDTO;
import com.dailycodework.agroshop.controller.dto.payments.CreatePreferenceRequestDTO.BackUrlsDTO;
import com.dailycodework.agroshop.controller.dto.payments.CreatePreferenceRequestDTO.DeliveryAddressDTO;
import com.dailycodework.agroshop.controller.dto.payments.CreatePreferenceRequestDTO.PayerDTO;
import com.dailycodework.agroshop.controller.dto.payments.CreatePreferenceResponseDTO;
import com.dailycodework.agroshop.controller.dto.payments.ItemDTO;
import com.dailycodework.agroshop.model.Address;
import com.dailycodework.agroshop.model.Order;
import com.dailycodework.agroshop.service.Order.OrderService;
import com.dailycodework.agroshop.service.User.UserService;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;


@Service
@Slf4j
@RequiredArgsConstructor
public class PaymentService {

    @Value("${app.frontend.url}")
    private String frontEndUrl;

    @Value("${mercadopago.notification-url}")
    private String notificationUrl;

    private final MercadoPagoClient mercadoPagoClient;
    private final OrderService pedidoService;
    private final UserService usuarioService;

    public CreatePreferenceResponseDTO createPreference(Long pedidoId){
        log.info("Entrou no service");
        Order pedido = pedidoService.buscaPedidoPorId(pedidoId);
        
        log.info("Pedido: "+ pedido);

        PayerDTO payer = new PayerDTO(
            pedido.getUser().getNome(),
            pedido.getUser().getEmail()
        );
        
        log.info("payer: "+ payer);

        BackUrlsDTO backUrls = new BackUrlsDTO(
            frontEndUrl + "/payment/success",
            frontEndUrl + "/payment/pending",
            frontEndUrl + "/payment/failure"
        );

        log.info("backUrls: "+backUrls);

        System.out.println(pedido.getEnderecoId());
        Address endereco = usuarioService.getEnderecoById(pedido.getUser(), pedido.getEnderecoId());

        DeliveryAddressDTO deliveryDTO = new DeliveryAddressDTO(
            endereco.getCep(),
            endereco.getEndereco(),
            endereco.getNumero(),
            endereco.getComplemento(),
            endereco.getCidade(),
            endereco.getEstado()
        );

        log.info("Deliverydto: "+ deliveryDTO);

        List<ItemDTO> items = pedido.getItens().stream()
            .map(item -> new ItemDTO(
                item.getId(),
                item.getProduct().getNome(),
                item.getQuantidade(),
                item.getPreco()
            ))
            .collect(Collectors.toList());

        items.add(new ItemDTO(
            Long.valueOf(00),
            "Frete",
            1,
            pedido.getFrete()
        ));    


        log.info("Items encontrados: {} itens", items.size());
        items.forEach(item -> 
            log.info("  Item: ID={}, Nome={}, Quantidade={}, Preço={}",
                item.id(), item.title(), item.quantity(), item.unitPrice())
        );

        CreatePreferenceRequestDTO request = new CreatePreferenceRequestDTO(
            pedido.getUser().getId(),
            pedido.getValorTotal(),
            payer,
            backUrls,
            deliveryDTO,
            notificationUrl,
            items
        );
        
        log.info("request criado");

        String orderNumber = pedido.getId().toString();

        try{
            CreatePreferenceResponseDTO responseDTO = mercadoPagoClient
                        .createPreference(request, orderNumber.toString());

            return responseDTO;
        } catch(Exception e){
            log.error("Error no service: ", e.getMessage());
        }
        return null;
    }
}
