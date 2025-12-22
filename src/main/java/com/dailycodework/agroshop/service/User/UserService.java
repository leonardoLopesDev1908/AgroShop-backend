package com.dailycodework.agroshop.service.User;

import java.util.List;
import java.util.Optional;
import java.util.UUID;
import java.util.stream.Collectors;

import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import com.dailycodework.agroshop.controller.dto.register.AddressRegisterDTO;
import com.dailycodework.agroshop.controller.dto.register.UserRegisterDTO;
import com.dailycodework.agroshop.controller.dto.search.AddressSearchDTO;
import com.dailycodework.agroshop.controller.dto.search.UserSearchDTO;
import com.dailycodework.agroshop.controller.dto.update.UserUpdateDTO;
import com.dailycodework.agroshop.controller.mapper.AddressMapper;
import com.dailycodework.agroshop.controller.mapper.UserMapper;
import com.dailycodework.agroshop.model.Address;
import com.dailycodework.agroshop.model.Role;
import com.dailycodework.agroshop.model.User;
import com.dailycodework.agroshop.repository.AddressRepository;
import com.dailycodework.agroshop.repository.RoleRepository;
import com.dailycodework.agroshop.repository.UserRepository;

import jakarta.persistence.EntityNotFoundException;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class UserService implements IUserService {

    private final UserRepository repository;
    private final UserMapper mapper;
    private final UserValidator validator;
    private final PasswordEncoder passwordEncoder;
    private final RoleRepository roleRepository;
    private final AddressRepository enderecoRepository;
    private final AddressMapper enderecoMapper;

    @Override
    @Transactional
    public UserSearchDTO addUsuario(UserRegisterDTO dto) {        
        User usuario = mapper.toEntity(dto);
        validator.validar(usuario);
        
        if(usuario.getRoles().isEmpty() || usuario.getRoles() == null){
            Role role = roleRepository.getByNome("Cliente");
            usuario.getRoles().add(role);
        }

        usuario.setSenha(passwordEncoder.encode(dto.senha()));
        
        List<Address> end = dto.endereco()
                                    .stream()
                                    .map(enderecoMapper::toEntity)
                                    .peek(endereco -> endereco.setUser(usuario))
                                    .collect(Collectors.toList());
        usuario.setEndereco(end);

        return mapper.toDTO(repository.save(usuario));
    }

    @Override
    public AddressSearchDTO cadastraEndereco(AddressRegisterDTO dto, User user){
        Address endereco = enderecoMapper.toEntity(dto);
        endereco.setUser(user);
        return enderecoMapper.toDTO(enderecoRepository.save(endereco));
    }

    @Override
    @Transactional
    public UserSearchDTO atualizarUsuario(UserUpdateDTO dto, User usuario) {
        validator.checkSenha(dto, usuario);
        
        mapper.updateUsuarioFromDto(dto, usuario);
        return mapper.toDTO(repository.save(usuario));
    }

    @Override
    public UserSearchDTO atualizarSenha(User user, String email,
                                             String senhaAtual, String senhaNova){
        validator.validarTrocaSenha(user, email, senhaAtual);
        user.setSenha(passwordEncoder.encode(senhaNova));
        return mapper.toDTO(repository.save(user));
    }

    @Override
    public User getAuthenticatedUsuario(){
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String email = authentication.getName();
        return Optional.ofNullable(repository.findByEmail(email))
                    .orElseThrow(() -> new EntityNotFoundException("Login necessário"));
    }

    @Override
    public List<AddressSearchDTO> getEnderecos(User usuario){
        List<Address> enderecos = enderecoRepository.getAddressByUser(usuario);
        return enderecos.stream()   
                        .map(enderecoMapper::toDTO)
                        .collect(Collectors.toList());
    }

    @Override 
    public Address getEnderecoById(User usuario, UUID id){
        System.out.println("getEnderecoById");
        List<Address> enderecos = enderecoRepository.getAddressByUser(usuario);
        for(Address endereco : enderecos){
            System.out.println(endereco.getId()+" " + id);
            if(endereco.getId().equals(id)){
                return endereco;
            }
        }
        return null;
    }

    @Override
    public UserSearchDTO buscarPorId(UUID id){
        User user =  repository.findById(id).orElseThrow(()->{
            throw new EntityNotFoundException("Usuário não encontrado");
        });
        return mapper.toDTO(user);
    }

    @Override
    public List<UserSearchDTO> buscarPorNome(String nome) {
        return repository
                    .findByNome(nome)
                    .stream()
                    .map(mapper::toDTO)
                    .collect(Collectors.toList());
    }

    @Override
    public UserSearchDTO buscarPorEmailDTO(String email){
        return Optional.ofNullable(mapper.toDTO(repository.findByEmail(email)))
                .orElseThrow(() -> new EntityNotFoundException("Usuário não encontrado"));
    }    

    @Override 
    public User buscarPorEmail(String email){
        return Optional.ofNullable(repository.findByEmail(email))
                .orElseThrow(() -> new EntityNotFoundException("Usuário não encontrado"));
    }

    @Override
    public void deletarUsuario(UUID id) {
        repository.findById(id).ifPresentOrElse(repository::delete,
            () -> new EntityNotFoundException("Usuário não encontrado"));
    }        

}
