package com.dailycodework.agroshop.controller.controllers;

import java.util.List;
import java.util.UUID;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.dailycodework.agroshop.controller.dto.pesquisa.CartItemSearchDTO;
import com.dailycodework.agroshop.controller.dto.update.CartItemUpdateDTO;
import com.dailycodework.agroshop.model.Cart;
import com.dailycodework.agroshop.model.User;
import com.dailycodework.agroshop.response.ApiResponse;
import com.dailycodework.agroshop.service.Cart.ICartItemService;
import com.dailycodework.agroshop.service.Cart.ICartService;
import com.dailycodework.agroshop.service.User.IUserService;

import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("${api.prefix}/usuario")
@RequiredArgsConstructor
public class CartController {

    private final ICartItemService cartItemService; 
    private final ICartService service;
    private final IUserService userService;

    @GetMapping("/me/carrinho")
    public ResponseEntity<ApiResponse> buscarCarrinhoUsuario(@PathVariable UUID usuarioId){
        User user = userService.getAuthenticatedUsuario();
        Cart carrinho = service.buscarPorIdUsuario(user);
        return ResponseEntity.ok(new ApiResponse("Sucesso", carrinho));
    }

    @DeleteMapping("/me/carrinho/itens")
    public ResponseEntity<ApiResponse> limparCarrinho(){
        User usuario = userService.getAuthenticatedUsuario();
        Cart carrinho =  usuario.getCarrinho();
        service.limparCarrinho(carrinho.getId());
        return ResponseEntity.status(HttpStatus.NO_CONTENT)
            .body(new ApiResponse("Sucesso!", null));
    }

    @GetMapping("/me/carrinho/itens")
    public ResponseEntity<ApiResponse> buscarTodosItens(){
        String email = SecurityContextHolder.getContext().getAuthentication().getName();
        List<CartItemSearchDTO> itens = service.todosItens(email);
        return ResponseEntity.ok(new ApiResponse("Sucesso!", itens));
    }

        @PostMapping("/me/carrinho/item")
    public ResponseEntity<ApiResponse> adicionarItemCarrinho(@RequestParam Long produtoId, @RequestParam int quantidade){
        User usuario = userService.getAuthenticatedUsuario();
        Cart carrinho = service.novoCarro(usuario);    
        cartItemService.adicionarItem(carrinho.getId(), produtoId, quantidade);
        return ResponseEntity.ok(new ApiResponse("Sucesso!", null));
    }

    @DeleteMapping("/me/carrinho/item/{produtoId}")
    public ResponseEntity<ApiResponse> excluirItem(@PathVariable Long produtoId){
        User usuario = userService.getAuthenticatedUsuario();
        Cart carrinho = service.novoCarro(usuario);
        cartItemService.removerItem(carrinho.getId(), produtoId);
        return ResponseEntity.ok(new ApiResponse("Sucesso!", null));
    }

    @PutMapping("/me/carrinho")
    public ResponseEntity<ApiResponse> atualizarQuantidade(@RequestBody CartItemUpdateDTO dto){
        User user = userService.getAuthenticatedUsuario();
        Cart carrinho = service.buscarPorIdUsuario(user);
        cartItemService.atualizarQuantidade(carrinho.getId(), dto.produtoId(), dto.quantidade());
        return ResponseEntity.ok(new ApiResponse("Sucesso!", null));
    }
}
