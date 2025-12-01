package com.dailycodework.agroshop.service.Frete;

import java.io.IOException;
import java.util.List;

import com.dailycodework.agroshop.controller.dto.pesquisa.FreteDTO;

public interface IFreteService {
    
    List<FreteDTO> freteProduto(Long idProduto, String cepDestino) throws IOException, InterruptedException;
    List<FreteDTO> freteItensCarrinho(String cepDestino);
}
