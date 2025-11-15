package com.dailycodework.agroshop.controller.controllers;

import java.util.List;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.dailycodework.agroshop.controller.dto.cadastro.AvaliacaoCadastroDTO;
import com.dailycodework.agroshop.controller.dto.pesquisa.AvaliacaoPesquisaDTO;
import com.dailycodework.agroshop.model.Usuario;
import com.dailycodework.agroshop.response.ApiResponse;
import com.dailycodework.agroshop.service.Avaliacao.AvaliacaoService;
import com.dailycodework.agroshop.service.Usuario.UsuarioService;

import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("${api.prefix}/avaliacoes")
@RequiredArgsConstructor
public class AvaliacaoController {
    
    private final AvaliacaoService service;
    private final UsuarioService userService;

    @GetMapping("/avaliacoes/{idProduto}")
    public ResponseEntity<ApiResponse> getComentarios(@PathVariable Long idProduto){
        List<AvaliacaoPesquisaDTO> comentarios = service.findAvaliacoes(idProduto);
        return ResponseEntity.ok(new ApiResponse("Sucesso!", comentarios));
    } 

    @PostMapping("/avaliar/{idProduto}")
    public ResponseEntity<ApiResponse> fazerComentario(@RequestBody AvaliacaoCadastroDTO dto,
                                                       @PathVariable Long idProduto){
        AvaliacaoPesquisaDTO comentario = service.addAvaliacao(dto, idProduto);
        return ResponseEntity.ok(new ApiResponse("Sucesso!", comentario));
    }

    @DeleteMapping("/avaliacao/excluir")
    public ResponseEntity<ApiResponse> excluirComentario(@RequestBody AvaliacaoPesquisaDTO dto){
        service.deleteAvaliacao(dto);
        return ResponseEntity.ok(new ApiResponse("Sucesso!", dto));
    }

    @GetMapping("/existe/{idProduto}")
    public ResponseEntity<ApiResponse> existeAvaliacao(@PathVariable Long idProduto){
        Usuario user = userService.getAuthenticatedUsuario();
        boolean response = service.verificarAvaliacao(user, idProduto);
        return ResponseEntity.ok(new ApiResponse("Sucesso!", response));
    }
}
