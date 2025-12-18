package com.dailycodework.agroshop.controller.dto.payments;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class ProccessNotificationResponseDTO {
    boolean success;
    String updatedStatus;
}
