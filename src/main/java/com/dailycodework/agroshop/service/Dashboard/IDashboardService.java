package com.dailycodework.agroshop.service.Dashboard;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.HashMap;

public interface IDashboardService {
    
    BigDecimal totalVendas(LocalDate dataInicio, LocalDate dataFim);
    HashMap<String, Long> produtosMaisVendidos(LocalDate dataInicio, LocalDate dataFim);
    //BigDecimal getLucro();
    HashMap<String, BigDecimal> vendasPorMes(Integer ano);
    Integer totalProduto();
    Integer totalClientes();
}
