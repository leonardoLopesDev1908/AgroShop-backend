package com.dailycodework.agroshop.controller.controllers;

import java.util.List;

import org.apache.maven.wagon.authorization.AuthorizationException;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.dailycodework.agroshop.controller.dto.pesquisa.AvaliationSearchDTO;
import com.dailycodework.agroshop.controller.dto.register.AvaliationRegisterDTO;
import com.dailycodework.agroshop.model.User;
import com.dailycodework.agroshop.response.ApiResponse;
import com.dailycodework.agroshop.service.Avaliation.AvaliationService;
import com.dailycodework.agroshop.service.User.UserService;
import com.mercadopago.net.HttpStatus;

import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("${api.prefix}/produtos")
@RequiredArgsConstructor
public class AvaliacaoController {
    
    private final AvaliationService service;
    private final UserService userService;

    @GetMapping("/{produtoId}/avaliacoes")
    public ResponseEntity<ApiResponse> getComentarios(@PathVariable Long produtoId){
        List<AvaliationSearchDTO> comentarios = service.findAvaliacoes(produtoId);
        return ResponseEntity.ok(new ApiResponse("Sucesso!", comentarios));
    } 

    @PostMapping("/{produtoId}/avaliacoes")
    public ResponseEntity<ApiResponse> fazerComentario(@RequestBody AvaliationRegisterDTO dto,
                                                       @PathVariable Long produtoId){
        AvaliationSearchDTO comentario = service.addAvaliacao(dto, produtoId);
        return ResponseEntity.status(HttpStatus.CREATED)
                        .body(new ApiResponse("Sucesso!", comentario));
    }

    @DeleteMapping("/avaliacoes/{avaliacaoId}")
    public ResponseEntity<ApiResponse> excluirComentario(@PathVariable Long avaliacaoId) throws AuthorizationException{
        User user = userService.getAuthenticatedUsuario();
        service.deleteAvaliacao(user, avaliacaoId);
        return ResponseEntity.status(HttpStatus.NO_CONTENT)
                            .body(new ApiResponse("Avaliação deletada",null));
    }

    @GetMapping("/{idProduto}/avaliacoes/me")
    public ResponseEntity<ApiResponse> existeAvaliacao(@PathVariable Long idProduto){
        User user = userService.getAuthenticatedUsuario();
        boolean response = service.verificarAvaliacao(user, idProduto);
        return ResponseEntity.ok(new ApiResponse("Sucesso!", response));
    }
}
