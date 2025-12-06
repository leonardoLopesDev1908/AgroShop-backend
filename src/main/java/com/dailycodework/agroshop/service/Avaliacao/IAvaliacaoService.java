package com.dailycodework.agroshop.service.Avaliacao;

import java.util.List;

import org.apache.maven.wagon.authorization.AuthorizationException;

import com.dailycodework.agroshop.controller.dto.cadastro.AvaliacaoCadastroDTO;
import com.dailycodework.agroshop.controller.dto.pesquisa.AvaliacaoPesquisaDTO;
import com.dailycodework.agroshop.model.Usuario;

public interface IAvaliacaoService {
    List<AvaliacaoPesquisaDTO> findAvaliacoes(Long idProduto);
    
    AvaliacaoPesquisaDTO addAvaliacao(AvaliacaoCadastroDTO dto, Long idProduto);

    void deleteAvaliacao(Usuario user, Long id) throws AuthorizationException;

    boolean verificarAvaliacao(Usuario user, Long idProduto);
}
