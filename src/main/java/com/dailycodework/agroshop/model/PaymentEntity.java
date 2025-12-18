package com.dailycodework.agroshop.model;

import com.dailycodework.agroshop.model.enums.PaymentStatus;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;
import lombok.NoArgsConstructor;
import lombok.Builder;


@Entity
@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Table
public class PaymentEntity {
    
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)

    @Column
    private String id;

    @Column
    private String orderId;

    @Column
    @Enumerated(EnumType.STRING)
    private PaymentStatus status;

    @Column
    private String amount;

    @Column
    private String statusDetail;
    
    @ManyToOne
    private Payer payer;

    @Column
    private String paymentMethodId;
}
