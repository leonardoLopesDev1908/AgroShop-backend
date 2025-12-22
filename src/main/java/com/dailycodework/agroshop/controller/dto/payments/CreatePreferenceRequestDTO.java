package com.dailycodework.agroshop.controller.dto.payments;

import java.math.BigDecimal;
import java.util.List;
import java.util.UUID;


public record CreatePreferenceRequestDTO (

    UUID userId,
    BigDecimal total,
    PayerDTO payer,
    BackUrlsDTO backUrls,
    DeliveryAddressDTO deliveryAddress,
    String notificationUrl,
    List<ItemDTO> items
)   {
    
    public record PayerDTO(
        String name, 
        String email
    ) {}

    public record BackUrlsDTO(
        String success, 
        String pending,
        String failure
    ) {}

    public record DeliveryAddressDTO(
        String zipCode,
        String street,
        String neighborhood,
        String number,
        String complement,
        String city,
        String state
    ) {}
}
