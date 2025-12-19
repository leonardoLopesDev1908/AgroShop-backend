package com.dailycodework.agroshop.controller.controllers;

import java.util.List;
import java.util.UUID;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.dailycodework.agroshop.controller.dto.pesquisa.AddressSearchDTO;
import com.dailycodework.agroshop.controller.dto.pesquisa.UserSearchDTO;
import com.dailycodework.agroshop.controller.dto.register.AddressRegisterDTO;
import com.dailycodework.agroshop.controller.dto.register.UserRegisterDTO;
import com.dailycodework.agroshop.controller.dto.update.UpdatePassword;
import com.dailycodework.agroshop.controller.dto.update.UserUpdateDTO;
import com.dailycodework.agroshop.controller.mapper.UserMapper;
import com.dailycodework.agroshop.model.User;
import com.dailycodework.agroshop.response.ApiResponse;
import com.dailycodework.agroshop.service.User.IUserService;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;

@RestController
@RequiredArgsConstructor
@RequestMapping("${api.prefix}/usuarios")
public class UserController {
    
    private final IUserService service;
    private final UserMapper mapper;

    @GetMapping("/me/dados")
    public ResponseEntity<ApiResponse> getDados(){
        User usuario = service.getAuthenticatedUsuario();
        UserSearchDTO dto = mapper.toDTO(usuario);
        return ResponseEntity.ok(new ApiResponse("Sucesso!", dto));
    }

    @PutMapping("/me/senha")
    public ResponseEntity<ApiResponse> alterarSenha(@RequestBody UpdatePassword dto){
        System.out.println("CONTROLLER");
        User user = service.getAuthenticatedUsuario();
        UserSearchDTO dtoUser = service.atualizarSenha(user, dto.email(),
                                                     dto.senhaAtual(), dto.senhaNova());
        return ResponseEntity.ok(new ApiResponse("Sucesso!", dtoUser));                                                
    }

    @GetMapping("/usuario/{nome}")
    public ResponseEntity<ApiResponse> getUsuariosPorNome(@PathVariable String nome){
        List<UserSearchDTO> usuarios = service.buscarPorNome(nome);
        return ResponseEntity.ok(new ApiResponse("Sucesso!", usuarios));
    }

    @PostMapping("/cadastro")
    public ResponseEntity<ApiResponse> cadastrarUsuario(@Valid @RequestBody UserRegisterDTO dto){
        UserSearchDTO usuario = service.addUsuario(dto);
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(new ApiResponse("Sucesso!", usuario));
    }

    @DeleteMapping("/usuario/{id}")
    public ResponseEntity<ApiResponse> deleteUsuario(@PathVariable UUID id){
        service.deletarUsuario(id);
        return ResponseEntity.status(HttpStatus.NO_CONTENT).body(new ApiResponse("Deletado!", null));
    }

    @GetMapping("/usuario/{id}")
    public ResponseEntity<ApiResponse> getUsuarioPorEmail(@PathVariable UUID id){
        UserSearchDTO dto = service.buscarPorId(id);
        return ResponseEntity.ok(new ApiResponse("Sucesso!", dto));
    }

    @GetMapping("/me/enderecos")
    public ResponseEntity<ApiResponse> getEnderecoUsuario(){
        User usuario = service.getAuthenticatedUsuario();
        List<AddressSearchDTO> enderecos = service.getEnderecos(usuario);
        return ResponseEntity.ok(new ApiResponse("Sucesso!", enderecos));
    }

    @PostMapping("/me/endereco/cadastro")
    public ResponseEntity<ApiResponse> cadastrarEndereco(@RequestBody AddressRegisterDTO dto){
        User user = service.getAuthenticatedUsuario();
        AddressSearchDTO response = service.cadastraEndereco(dto, user);
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(new ApiResponse("Sucesso!", response));
    }

    @PutMapping("/me/atualizacao")
    public ResponseEntity<ApiResponse> atualizarUsuario(@RequestBody UserUpdateDTO dto){
        User user = service.getAuthenticatedUsuario();
        UserSearchDTO response = service.atualizarUsuario(dto, user);
        return ResponseEntity.ok(new ApiResponse("Sucesso!", response));
    }
}
