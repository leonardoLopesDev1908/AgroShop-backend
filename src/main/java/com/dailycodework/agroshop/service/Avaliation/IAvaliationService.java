package com.dailycodework.agroshop.service.Avaliation;

import java.util.List;

import org.apache.maven.wagon.authorization.AuthorizationException;

import com.dailycodework.agroshop.controller.dto.register.AvaliationRegisterDTO;
import com.dailycodework.agroshop.controller.dto.search.AvaliationSearchDTO;
import com.dailycodework.agroshop.model.User;

public interface IAvaliationService {
    List<AvaliationSearchDTO> findAvaliacoes(Long idProduto);
    
    AvaliationSearchDTO addAvaliacao(AvaliationRegisterDTO dto, Long idProduto);

    void deleteAvaliacao(User user, Long id) throws AuthorizationException;

    boolean verificarAvaliacao(User user, Long idProduto);
}
