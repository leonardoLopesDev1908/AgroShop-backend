package com.dailycodework.agroshop.service.Dashboard;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.HashMap;
import java.util.List;

import org.springframework.stereotype.Service;

import com.dailycodework.agroshop.repository.PedidoRepository;
import com.dailycodework.agroshop.repository.ProdutoRepository;
import com.dailycodework.agroshop.repository.UsuarioRepository;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class DashboardService implements IDashboardService {

    private final ProdutoRepository produtoRepository;
    private final PedidoRepository pedidoRepository;
    private final UsuarioRepository usuarioRepository;

    @Override
    public BigDecimal totalVendas(LocalDate dataInicio, LocalDate dataFim) {
        if(dataInicio == null){
            dataInicio = LocalDate.MIN;
        }
        if(dataFim == null){
            dataFim = LocalDate.MAX;
        }
        LocalDateTime dataStart = dataInicio.atStartOfDay();
        LocalDateTime dataEnd = dataFim.atStartOfDay();
        return pedidoRepository.totalVendas(dataStart, dataEnd);
    }

    @Override
    public HashMap<String, Long> produtosMaisVendidos(LocalDate dataInicio, LocalDate dataFim) {
        dataInicio = (dataInicio == null) ? LocalDate.now().withDayOfYear(1) : dataInicio;
        dataFim = (dataFim == null) ? LocalDate.now() : dataFim;

        LocalDateTime dataStart = dataInicio.atStartOfDay();
        LocalDateTime dataEnd = dataFim.atTime(LocalTime.MAX); 

        List<Object[]> resultado = pedidoRepository.produtosMaisVendidos(dataStart, dataEnd);
        HashMap<String, Long> produtos = new HashMap<>();
        for(Object[] o : resultado){
            String nome = String.valueOf(o[0]);
            Long qtd = (Long) o[1];
            produtos.put(nome, qtd);
        }
        return produtos;
    }

    @Override
    public HashMap<String, BigDecimal> vendasPorMes(Integer anoBusca) {
        HashMap<String, BigDecimal> vendas = new HashMap<>();
        for(Object[] row : pedidoRepository.vendasPorMes(anoBusca)){
            Integer ano = (Integer) row[0];
            Integer mes = (Integer) row[1];
            BigDecimal valor = (BigDecimal) row[2];
            String chave = String.format("%02d/%d", mes, ano);
            vendas.put(chave, valor);
        }
        return vendas;
    }

    @Override
    public Integer totalProduto() {
       return produtoRepository.totalProdutos();
    }

    @Override
    public Integer totalClientes() {
        return usuarioRepository.totalClientes();
    }
    
}
