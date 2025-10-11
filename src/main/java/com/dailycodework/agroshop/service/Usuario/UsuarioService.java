package com.dailycodework.agroshop.service.Usuario;

import java.util.List;
import java.util.Optional;
import java.util.UUID;
import java.util.stream.Collectors;

import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import com.dailycodework.agroshop.controller.dto.cadastro.UsuarioCadastroDTO;
import com.dailycodework.agroshop.controller.dto.pesquisa.UsuarioPesquisaDTO;
import com.dailycodework.agroshop.controller.dto.update.UsuarioUpdateDTO;
import com.dailycodework.agroshop.controller.mapper.EnderecoMapper;
import com.dailycodework.agroshop.controller.mapper.UsuarioMapper;
import com.dailycodework.agroshop.model.Endereco;
import com.dailycodework.agroshop.model.Usuario;
import com.dailycodework.agroshop.repository.UsuarioRepository;

import jakarta.persistence.EntityNotFoundException;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class UsuarioService implements IUsuarioService {

    private final UsuarioRepository repository;
    private final UsuarioMapper mapper;
    private final UsuarioValidator validator;
    private final PasswordEncoder passwordEncoder;
    private final EnderecoMapper enderecoMapper;

    @Override
    @Transactional
    public UsuarioPesquisaDTO addUsuario(UsuarioCadastroDTO dto) {
        Usuario usuario = mapper.toEntity(dto);
        validator.validar(usuario);
        
        usuario.setSenha(passwordEncoder.encode(dto.senha()));
        
        Endereco end = enderecoMapper.toEntity(dto.endereco());
        usuario.setEndereco(end);
        end.setUsuario(usuario);

        return mapper.toDTO(repository.save(usuario));
    }


    @Override
    @Transactional
    public Usuario atualizarUsuario(UsuarioUpdateDTO dto, UUID id) {
         Usuario usuarioExistente = repository.findById(id).orElseThrow(() ->{
            throw new EntityNotFoundException("Usuário não encontrado para o ID: " + id);
        });
        
        mapper.updateUsuarioFromDto(dto, usuarioExistente);

        return repository.save(usuarioExistente);

        /*
         * Implementar autenticação para atualizar senha
         * verificar se ele sabe a senha atual e então permitir
         */
    }

    @Override
    public void deletarUsuario(UUID id) {
        repository.findById(id).ifPresentOrElse(repository::delete,
            () -> new EntityNotFoundException("Usuário não encontrado"));
    }

    @Override
    public Usuario buscarPorId(UUID id){
        return repository.findById(id).orElseThrow(()->{
            throw new EntityNotFoundException("Usuário não encontrado");
        });
    }


    @Override
    public List<UsuarioPesquisaDTO> buscarPorNome(String nome) {
        return repository
                    .findByNome(nome)
                    .stream()
                    .map(mapper::toDTO)
                    .collect(Collectors.toList());
    }

    @Override
    public Usuario getAuthenticatedUsuario(){
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String email = authentication.getName();
        return Optional.ofNullable(repository.findByEmail(email))
                    .orElseThrow(() -> new EntityNotFoundException("Login necessário"));
    }

    @Override
    public UsuarioPesquisaDTO buscarPorEmail(String email){
        return Optional.ofNullable(mapper.toDTO(repository.findByEmail(email)))
                .orElseThrow(() -> new EntityNotFoundException("Usuário não encontrado"));
    }    
}
