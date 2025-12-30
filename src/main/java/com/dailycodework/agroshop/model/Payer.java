package com.dailycodework.agroshop.model;

import jakarta.persistence.Embeddable;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Entity
public class Payer {
    
    @Id
    String email;
    String firstName;
    String lastName;
    Identification identification;

    @Getter
    @Setter
    @Builder
    @Embeddable
    public static class Identification{
        String type;
        String number;
    }
}
