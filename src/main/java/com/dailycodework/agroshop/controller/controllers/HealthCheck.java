package com.dailycodework.agroshop.controller.controllers;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.dailycodework.agroshop.response.ApiResponse;

@RestController
@RequestMapping("/health")
public class HealthCheck {
    
    @GetMapping
    public ResponseEntity<ApiResponse> checkHealth(){
        return ResponseEntity.ok(new ApiResponse("Sucesso!", null));
    }
}
