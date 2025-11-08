package com.dailycodework.agroshop.controller.controllers;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.HashMap;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.dailycodework.agroshop.response.ApiResponse;
import com.dailycodework.agroshop.service.Dashboard.DashboardService;

import lombok.RequiredArgsConstructor;

@RequestMapping("${api.prefix}/dashboard")
@RestController
@RequiredArgsConstructor
public class DashboardController {
    
    private final DashboardService service;

    @GetMapping("/total-vendas")
    public ResponseEntity<ApiResponse> getTotalVendas(
                                            @RequestParam LocalDate dataInicio,
                                            @RequestParam LocalDate dataFim){
        BigDecimal totalVendas = service.totalVendas(dataInicio, dataFim);
        return ResponseEntity.ok(new ApiResponse("Sucesso!", totalVendas));
    }

    @GetMapping("/vendas-mes")
    public ResponseEntity<ApiResponse> getVendasPorMes(@RequestParam Integer anoBusca){
        HashMap<String, BigDecimal> dados = service.vendasPorMes(anoBusca);
        return ResponseEntity.ok(new ApiResponse("Sucesso!", dados));
    }

    @GetMapping("/produtos-vendidos")
    public ResponseEntity<ApiResponse> getProdutosMaisVendidos(
                                            @RequestParam LocalDate dataInicio,
                                            @RequestParam LocalDate dataFim){
        HashMap<String, Long> dados = service.produtosMaisVendidos(dataInicio, dataFim);
        return ResponseEntity.ok(new ApiResponse("Sucesso!", dados));
    }

    @GetMapping("/total-produtos")
    public ResponseEntity<ApiResponse> getTotalProdutos(){
        Integer totalProdutos = service.totalProduto();
        return ResponseEntity.ok(new ApiResponse("Sucesso!", totalProdutos));
    }

    @GetMapping("/total-clientes")
    public ResponseEntity<ApiResponse> getTotalClientes(){
        Integer totalClientes = service.totalClientes();
        return ResponseEntity.ok(new ApiResponse("Sucesso!", totalClientes));
    }
}
