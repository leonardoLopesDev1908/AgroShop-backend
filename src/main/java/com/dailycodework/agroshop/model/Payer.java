package com.dailycodework.agroshop.model;

import org.hibernate.annotations.NaturalId;

import jakarta.persistence.Embeddable;
import jakarta.persistence.EmbeddedId;
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
