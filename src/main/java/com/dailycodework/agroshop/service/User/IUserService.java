package com.dailycodework.agroshop.service.User;

import java.util.List;
import java.util.UUID;

import com.dailycodework.agroshop.controller.dto.register.AddressRegisterDTO;
import com.dailycodework.agroshop.controller.dto.register.UserRegisterDTO;
import com.dailycodework.agroshop.controller.dto.search.AddressSearchDTO;
import com.dailycodework.agroshop.controller.dto.search.UserSearchDTO;
import com.dailycodework.agroshop.controller.dto.update.UserUpdateDTO;
import com.dailycodework.agroshop.model.Address;
import com.dailycodework.agroshop.model.User;

public interface IUserService {
    UserSearchDTO addUsuario(UserRegisterDTO dto);    
    AddressSearchDTO cadastraEndereco(AddressRegisterDTO dto, User user);
    UserSearchDTO atualizarUsuario(UserUpdateDTO dto, User usuario);
    UserSearchDTO atualizarSenha(User usuario, String email, String senhaAtual, String senhaNova);
    User getAuthenticatedUsuario();
    List<AddressSearchDTO> getEnderecos(User usuario);
    Address getEnderecoById(User usuario, UUID id);
    UserSearchDTO buscarPorId(UUID id);
    List<UserSearchDTO> buscarPorNome(String nome);
    UserSearchDTO buscarPorEmailDTO(String email);
    User buscarPorEmail(String email);
    void deletarUsuario(UUID id);
}
