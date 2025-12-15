package com.dailycodework.agroshop.controller.controllers;

import org.springframework.http.ResponseEntity;
import static org.springframework.http.ResponseEntity.ok;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import jakarta.servlet.http.HttpServletRequest;

@RestController
@RequestMapping("${api.prefix}/csrf")
public class CsrfController {
    
    @GetMapping
    public ResponseEntity<Void> getCsrfToken(HttpServletRequest request){
        return ok().build();
    }
}
