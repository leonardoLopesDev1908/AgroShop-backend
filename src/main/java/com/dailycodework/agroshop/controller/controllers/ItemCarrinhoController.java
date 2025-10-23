package com.dailycodework.agroshop.controller.controllers;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.dailycodework.agroshop.controller.dto.update.ItemCarrinhoUpdateDTO;
import com.dailycodework.agroshop.model.Carrinho;
import com.dailycodework.agroshop.model.Usuario;
import com.dailycodework.agroshop.response.ApiResponse;
import com.dailycodework.agroshop.service.Carrinho.ICarrinhoService;
import com.dailycodework.agroshop.service.Carrinho.IItemCarrinhoService;
import com.dailycodework.agroshop.service.Usuario.IUsuarioService;

import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("${api.prefix}/itens")
@RequiredArgsConstructor
public class ItemCarrinhoController {
    
    private final IItemCarrinhoService service; 
    private final IUsuarioService usuarioService;
    private final ICarrinhoService carrinhoService;

    @PostMapping("/item/cadastrar")
    public ResponseEntity<ApiResponse> adicionarItemCarrinho(@RequestParam Long produtoId, @RequestParam int quantidade){
        Usuario usuario = usuarioService.getAuthenticatedUsuario();
        Carrinho carrinho = carrinhoService.novoCarro(usuario);    
        service.adicionarItem(carrinho.getId(), produtoId, quantidade);
        return ResponseEntity.ok(new ApiResponse("Sucesso!", null));
    }

    @DeleteMapping("/carrinho/item/{produtoId}/excluir")
    public ResponseEntity<ApiResponse> excluirItem(@PathVariable Long produtoId){
        Usuario usuario = usuarioService.getAuthenticatedUsuario();
        Carrinho carrinho = carrinhoService.novoCarro(usuario);
        service.removerItem(carrinho.getId(), produtoId);
        return ResponseEntity.ok(new ApiResponse("Sucesso!", null));
    }

    @PutMapping("/carrinho/atualizar")
    public ResponseEntity<ApiResponse> atualizarQuantidade(@RequestBody ItemCarrinhoUpdateDTO dto){
        Usuario user = usuarioService.getAuthenticatedUsuario();
        Carrinho carrinho = carrinhoService.buscarPorIdUsuario(user.getId());
        service.atualizarQuantidade(carrinho.getId(), dto.produtoId(), dto.quantidade());
        return ResponseEntity.ok(new ApiResponse("Sucesso!", null));
    }

}
