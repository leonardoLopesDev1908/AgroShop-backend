package com.dailycodework.agroshop.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.dailycodework.agroshop.model.Image;

@Repository
public interface ImageRepository extends JpaRepository<Image, Long>{
    
}
