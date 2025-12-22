package com.dailycodework.agroshop.repository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

import org.springframework.data.jpa.repository.JpaRepository;

import com.dailycodework.agroshop.model.Address;
import com.dailycodework.agroshop.model.User;

public interface AddressRepository extends JpaRepository<Address, UUID> {
    
    List<Address> getAddressByUser(User usuario);
    Optional<Address> findByZipcodeAndNumberAndComplement(String zipcode, 
                                                        String number, 
                                                        String complement);
    Optional<Address> findByZipcodeAndNumber(String zipcode, String number);
}
