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

import com.dailycodework.agroshop.controller.dto.cadastro.UsuarioCadastroDTO;
import com.dailycodework.agroshop.controller.dto.pesquisa.UsuarioPesquisaDTO;
import com.dailycodework.agroshop.controller.dto.update.UsuarioUpdateDTO;
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

    // @GetMapping("usuario/{id}/usuario")
    // public ResponseEntity<ApiResponse> getUsuarioPorId(@PathVariable UUID id){
    //     UsuarioPesquisaDTO usuario = service.buscarPorId(id);
    //     return ResponseEntity.ok(new ApiResponse("Sucesso!", usuario));
    // }

    @GetMapping("usuario/{nome}/usuarios")
    public ResponseEntity<ApiResponse> getUsuariosPorNome(@PathVariable String nome){
        List<UsuarioPesquisaDTO> usuarios = service.buscarPorNome(nome);
        return ResponseEntity.ok(new ApiResponse("Sucesso!", usuarios));
    }


    @PostMapping("/cadastrar")
    public ResponseEntity<ApiResponse> cadastrarUsuario(@Valid @RequestBody UsuarioCadastroDTO dto){
        System.out.println(dto.endereco());
        UsuarioPesquisaDTO usuario = service.addUsuario(dto);
        return ResponseEntity.ok(new ApiResponse("Sucesso!", usuario));
    }

    @DeleteMapping("/usuario/{id}/deletar")
    public ResponseEntity<ApiResponse> deleteUsuario(@PathVariable UUID id){
        service.deletarUsuario(id);
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(new ApiResponse("Deletado!", null));
    }

    @PutMapping("/usuario/{id}/atualizar")
    public ResponseEntity<ApiResponse> atualizaUsuario(@PathVariable UUID id, @RequestBody UsuarioUpdateDTO dto){
        Usuario usuario = service.atualizarUsuario(dto, id);
        return ResponseEntity.ok(new ApiResponse("Sucesso!", usuario));
    }

    @GetMapping("/usuario/{email}")
    public ResponseEntity<ApiResponse> getUsuarioPorEmail(@PathVariable String email){
        UsuarioPesquisaDTO dto = service.buscarPorEmailDTO(email);
        return ResponseEntity.ok(new ApiResponse("Sucesso!", dto));
    }
}
