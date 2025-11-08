package com.dailycodework.agroshop.service.Usuario;

import java.util.List;
import java.util.UUID;

import com.dailycodework.agroshop.controller.dto.cadastro.EnderecoCadastroDTO;
import com.dailycodework.agroshop.controller.dto.cadastro.UsuarioCadastroDTO;
import com.dailycodework.agroshop.controller.dto.pesquisa.EnderecoPesquisaDTO;
import com.dailycodework.agroshop.controller.dto.pesquisa.UsuarioPesquisaDTO;
import com.dailycodework.agroshop.controller.dto.update.UsuarioUpdateDTO;
import com.dailycodework.agroshop.model.Usuario;

public interface IUsuarioService {
    UsuarioPesquisaDTO addUsuario(UsuarioCadastroDTO dto);    
    EnderecoPesquisaDTO cadastraEndereco(EnderecoCadastroDTO dto, Usuario user);
    UsuarioPesquisaDTO atualizarUsuario(UsuarioUpdateDTO dto, Usuario usuario);
    UsuarioPesquisaDTO atualizarSenha(Usuario usuario, String email, String senhaAtual, String senhaNova);
    Usuario getAuthenticatedUsuario();
    List<EnderecoPesquisaDTO> getEnderecos(Usuario usuario);
    Usuario buscarPorId(UUID id);
    List<UsuarioPesquisaDTO> buscarPorNome(String nome);
    UsuarioPesquisaDTO buscarPorEmailDTO(String email);
    Usuario buscarPorEmail(String email);
    void deletarUsuario(UUID id);
}
