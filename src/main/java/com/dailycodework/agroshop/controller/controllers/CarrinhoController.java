package com.dailycodework.agroshop.controller.controllers;

import java.util.List;
import java.util.UUID;

import org.springframework.http.ResponseEntity;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.dailycodework.agroshop.controller.dto.pesquisa.ItemCarrinhoPesquisaDTO;
import com.dailycodework.agroshop.model.Carrinho;
import com.dailycodework.agroshop.response.ApiResponse;
import com.dailycodework.agroshop.service.Carrinho.ICarrinhoService;

import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("${api.prefix}/carrinho")
@RequiredArgsConstructor
public class CarrinhoController {

    private final ICarrinhoService service;

    @GetMapping("/usuario/{usuarioId}/carrinho")
    public ResponseEntity<ApiResponse> buscarCarrinhoUsuario(@PathVariable UUID usuarioId){
        Carrinho carrinho = service.buscarPorIdUsuario(usuarioId);
        return ResponseEntity.ok(new ApiResponse("Sucesso", carrinho));
    }

    @DeleteMapping("/carrinho/{id}/limpar")
    public void limpar(@PathVariable Long id){
        service.limparCarrinho(id);
    }

    @GetMapping("/itens")
    public ResponseEntity<ApiResponse> buscarTodosItens(){
        String email = SecurityContextHolder.getContext().getAuthentication().getName();
        List<ItemCarrinhoPesquisaDTO> itens = service.todosItens(email);
        return ResponseEntity.ok(new ApiResponse("Sucesso!", itens));
    }

}
