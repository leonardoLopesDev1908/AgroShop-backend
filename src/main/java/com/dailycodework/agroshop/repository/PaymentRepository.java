package com.dailycodework.agroshop.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.dailycodework.agroshop.model.PaymentEntity;

@Repository
public interface PaymentRepository extends JpaRepository<PaymentEntity, String> {

}
