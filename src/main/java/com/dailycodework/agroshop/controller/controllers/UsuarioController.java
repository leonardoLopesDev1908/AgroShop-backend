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

import com.dailycodework.agroshop.controller.dto.cadastro.EnderecoCadastroDTO;
import com.dailycodework.agroshop.controller.dto.cadastro.UsuarioCadastroDTO;
import com.dailycodework.agroshop.controller.dto.pesquisa.EnderecoPesquisaDTO;
import com.dailycodework.agroshop.controller.dto.pesquisa.UsuarioPesquisaDTO;
import com.dailycodework.agroshop.controller.dto.update.AlterarSenhaDTO;
import com.dailycodework.agroshop.controller.dto.update.UsuarioUpdateDTO;
import com.dailycodework.agroshop.controller.mapper.UsuarioMapper;
import com.dailycodework.agroshop.model.Usuario;
import com.dailycodework.agroshop.response.ApiResponse;
import com.dailycodework.agroshop.service.Usuario.IUsuarioService;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;

@RestController
@RequiredArgsConstructor
@RequestMapping("${api.prefix}/usuarios")
public class UsuarioController {
    
    private final IUsuarioService service;
    private final UsuarioMapper mapper;

    @GetMapping("/me/dados")
    public ResponseEntity<ApiResponse> getDados(){
        Usuario usuario = service.getAuthenticatedUsuario();
        UsuarioPesquisaDTO dto = mapper.toDTO(usuario);
        return ResponseEntity.ok(new ApiResponse("Sucesso!", dto));
    }

    @PutMapping("/me/senha")
    public ResponseEntity<ApiResponse> alterarSenha(@RequestBody AlterarSenhaDTO dto){
        System.out.println("CONTROLLER");
        Usuario user = service.getAuthenticatedUsuario();
        UsuarioPesquisaDTO dtoUser = service.atualizarSenha(user, dto.email(),
                                                     dto.senhaAtual(), dto.senhaNova());
        return ResponseEntity.ok(new ApiResponse("Sucesso!", dtoUser));                                                
    }

    @GetMapping("/usuario/{nome}")
    public ResponseEntity<ApiResponse> getUsuariosPorNome(@PathVariable String nome){
        List<UsuarioPesquisaDTO> usuarios = service.buscarPorNome(nome);
        return ResponseEntity.ok(new ApiResponse("Sucesso!", usuarios));
    }

    @PostMapping("/cadastro")
    public ResponseEntity<ApiResponse> cadastrarUsuario(@Valid @RequestBody UsuarioCadastroDTO dto){
        UsuarioPesquisaDTO usuario = service.addUsuario(dto);
        return ResponseEntity.ok(new ApiResponse("Sucesso!", usuario));
    }

    @DeleteMapping("/usuario/{id}")
    public ResponseEntity<ApiResponse> deleteUsuario(@PathVariable UUID id){
        service.deletarUsuario(id);
        return ResponseEntity.status(HttpStatus.NO_CONTENT).body(new ApiResponse("Deletado!", null));
    }

    @GetMapping("/usuario/{id}")
    public ResponseEntity<ApiResponse> getUsuarioPorEmail(@PathVariable UUID id){
        UsuarioPesquisaDTO dto = service.buscarPorId(id);
        return ResponseEntity.ok(new ApiResponse("Sucesso!", dto));
    }

    @GetMapping("/me/enderecos")
    public ResponseEntity<ApiResponse> getEnderecoUsuario(){
        Usuario usuario = service.getAuthenticatedUsuario();
        List<EnderecoPesquisaDTO> enderecos = service.getEnderecos(usuario);
        return ResponseEntity.ok(new ApiResponse("Sucesso!", enderecos));
    }

    @PostMapping("/me/endereco/cadastro")
    public ResponseEntity<ApiResponse> cadastrarEndereco(@RequestBody EnderecoCadastroDTO dto){
        Usuario user = service.getAuthenticatedUsuario();
        EnderecoPesquisaDTO response = service.cadastraEndereco(dto, user);
        return ResponseEntity.ok(new ApiResponse("Sucesso!", response));
    }

    @PutMapping("/me/atualizacao")
    public ResponseEntity<ApiResponse> atualizarUsuario(@RequestBody UsuarioUpdateDTO dto){
        Usuario user = service.getAuthenticatedUsuario();
        UsuarioPesquisaDTO response = service.atualizarUsuario(dto, user);
        return ResponseEntity.ok(new ApiResponse("Sucesso!", response));
    }
}
