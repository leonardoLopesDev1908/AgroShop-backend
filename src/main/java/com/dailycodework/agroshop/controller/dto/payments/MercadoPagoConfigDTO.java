package com.dailycodework.agroshop.controller.dto.payments;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
public class MercadoPagoConfigDTO {

    private String action;
    private String api_version;
    private Data data;
    private String date_created;
    private Long id;
    private boolean live_mode;
    private String type;
    private long user_id;

    @NoArgsConstructor
    @Getter
    @Setter
    public static class Data{
        private String id;
    }
}
