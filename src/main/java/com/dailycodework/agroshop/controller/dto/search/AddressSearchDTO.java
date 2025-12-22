package com.dailycodework.agroshop.controller.dto.search;

public record AddressSearchDTO(  
                    String street,
                    String neighborhood,
                    String number,
                    String complement,
                    String city,
                    String state,
                    String zipcode
) {}
