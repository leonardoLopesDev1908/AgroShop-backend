package com.dailycodework.agroshop.service.Usuario;

import java.util.List;
import java.util.UUID;

import com.dailycodework.agroshop.controller.dto.cadastro.UsuarioCadastroDTO;
import com.dailycodework.agroshop.controller.dto.pesquisa.UsuarioPesquisaDTO;
import com.dailycodework.agroshop.controller.dto.update.UsuarioUpdateDTO;
import com.dailycodework.agroshop.model.Usuario;

public interface IUsuarioService {
    UsuarioPesquisaDTO addUsuario(UsuarioCadastroDTO dto);    
    Usuario atualizarUsuario(UsuarioUpdateDTO dto, UUID id);
    // UsuarioPesquisaDTO buscarPorId(UUID id);
    Usuario buscarPorId(UUID id);
    List<UsuarioPesquisaDTO> buscarPorNome(String nome);
    void deletarUsuario(UUID id);
    Usuario getAuthenticatedUsuario();
    UsuarioPesquisaDTO buscarPorEmail(String email);
}
