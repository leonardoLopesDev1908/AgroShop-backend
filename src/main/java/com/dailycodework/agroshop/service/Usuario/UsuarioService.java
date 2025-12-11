package com.dailycodework.agroshop.service.Usuario;

import java.util.List;
import java.util.Optional;
import java.util.UUID;
import java.util.stream.Collectors;

import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import com.dailycodework.agroshop.controller.dto.cadastro.EnderecoCadastroDTO;
import com.dailycodework.agroshop.controller.dto.cadastro.UsuarioCadastroDTO;
import com.dailycodework.agroshop.controller.dto.pesquisa.EnderecoPesquisaDTO;
import com.dailycodework.agroshop.controller.dto.pesquisa.UsuarioPesquisaDTO;
import com.dailycodework.agroshop.controller.dto.update.UsuarioUpdateDTO;
import com.dailycodework.agroshop.controller.mapper.EnderecoMapper;
import com.dailycodework.agroshop.controller.mapper.UsuarioMapper;
import com.dailycodework.agroshop.model.Endereco;
import com.dailycodework.agroshop.model.Role;
import com.dailycodework.agroshop.model.Usuario;
import com.dailycodework.agroshop.repository.EnderecoRepository;
import com.dailycodework.agroshop.repository.RoleRepository;
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
    private final RoleRepository roleRepository;
    private final EnderecoRepository enderecoRepository;
    private final EnderecoMapper enderecoMapper;

    @Override
    @Transactional
    public UsuarioPesquisaDTO addUsuario(UsuarioCadastroDTO dto) {        
        Usuario usuario = mapper.toEntity(dto);
        validator.validar(usuario);
        
        if(usuario.getRoles().isEmpty() || usuario.getRoles() == null){
            Role role = roleRepository.getByNome("Cliente");
            usuario.getRoles().add(role);
        }

        usuario.setSenha(passwordEncoder.encode(dto.senha()));
        
        List<Endereco> end = dto.endereco()
                                    .stream()
                                    .map(enderecoMapper::toEntity)
                                    .peek(endereco -> endereco.setUsuario(usuario))
                                    .collect(Collectors.toList());
        usuario.setEndereco(end);

        return mapper.toDTO(repository.save(usuario));
    }

    @Override
    public EnderecoPesquisaDTO cadastraEndereco(EnderecoCadastroDTO dto, Usuario user){
        Endereco endereco = enderecoMapper.toEntity(dto);
        endereco.setUsuario(user);
        return enderecoMapper.toDTO(enderecoRepository.save(endereco));
    }

    @Override
    @Transactional
    public UsuarioPesquisaDTO atualizarUsuario(UsuarioUpdateDTO dto, Usuario usuario) {
        validator.checkSenha(dto, usuario);
        
        mapper.updateUsuarioFromDto(dto, usuario);
        return mapper.toDTO(repository.save(usuario));
    }

    @Override
    public UsuarioPesquisaDTO atualizarSenha(Usuario user, String email,
                                             String senhaAtual, String senhaNova){
        validator.validarTrocaSenha(user, email, senhaAtual);
        user.setSenha(passwordEncoder.encode(senhaNova));
        return mapper.toDTO(repository.save(user));
    }

    @Override
    public Usuario getAuthenticatedUsuario(){
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String email = authentication.getName();
        return Optional.ofNullable(repository.findByEmail(email))
                    .orElseThrow(() -> new EntityNotFoundException("Login necessário"));
    }

    @Override
    public List<EnderecoPesquisaDTO> getEnderecos(Usuario usuario){
        List<Endereco> enderecos = enderecoRepository.getEnderecoByUsuario(usuario);
        return enderecos.stream()   
                        .map(enderecoMapper::toDTO)
                        .collect(Collectors.toList());
    }

    @Override
    public UsuarioPesquisaDTO buscarPorId(UUID id){
        Usuario user =  repository.findById(id).orElseThrow(()->{
            throw new EntityNotFoundException("Usuário não encontrado");
        });
        return mapper.toDTO(user);
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
    public UsuarioPesquisaDTO buscarPorEmailDTO(String email){
        return Optional.ofNullable(mapper.toDTO(repository.findByEmail(email)))
                .orElseThrow(() -> new EntityNotFoundException("Usuário não encontrado"));
    }    

    @Override 
    public Usuario buscarPorEmail(String email){
        return Optional.ofNullable(repository.findByEmail(email))
                .orElseThrow(() -> new EntityNotFoundException("Usuário não encontrado"));
    }

    @Override
    public void deletarUsuario(UUID id) {
        repository.findById(id).ifPresentOrElse(repository::delete,
            () -> new EntityNotFoundException("Usuário não encontrado"));
    }        

}
