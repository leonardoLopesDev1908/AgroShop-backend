package com.dailycodework.agroshop.controller;

import java.util.List;

import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import com.dailycodework.agroshop.controller.controllers.CartController;
import com.dailycodework.agroshop.controller.dto.pesquisa.CartItemSearchDTO;
import com.dailycodework.agroshop.model.Cart;
import com.dailycodework.agroshop.model.User;
import com.dailycodework.agroshop.service.Cart.ICartService;
import com.dailycodework.agroshop.service.User.UserService;


@WebMvcTest(CartController.class)
public class CartControllerTest {
    
    @Autowired
    private MockMvc mockMvc;

    @MockitoBean
    private ICartService carrinhoService;
    
    @MockitoBean
    private UserService usuarioService;

    @Test
    @WithMockUser(username = "teste@gmail.com")
    void deveBuscarCarrinhoUsuarioLogado() throws Exception{
        User usuario = new User();
        Cart carrinho = new Cart();
        
        Mockito.when(usuarioService.getAuthenticatedUsuario())
            .thenReturn(usuario);
        
        Mockito.when(carrinhoService.buscarPorIdUsuario(usuario))
            .thenReturn(carrinho);
        
        mockMvc.perform(get("/api/v1/usuario/me/carrinho")
            .contentType(MediaType.APPLICATION_JSON))
            .andExpect(status().isOk())
            .andExpect(jsonPath("$.message").value("Sucesso"))
            .andExpect(jsonPath("$.data").exists());
    }

    @Test
    @WithMockUser(username = "teste@gmail.com")
    void deveLimparCarrinho() throws Exception{
        User usuario = new User();
        Cart carrinho = new Cart();

        Mockito.when(usuarioService.getAuthenticatedUsuario())
            .thenReturn(usuario);
        
        Mockito.when(carrinhoService.buscarPorIdUsuario(usuario))
            .thenReturn(carrinho);
        
        mockMvc.perform(delete("/api/v1/usuario/me/carrinho/limpar")
            .contentType(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());
    }

    @Test
    @WithMockUser(username = "teste@gmail.com")
    void deveBuscarTodosItens() throws Exception {
        String email = "teste@gmail.com";
        List<CartItemSearchDTO> itens = null;
        
        Mockito.when(carrinhoService.todosItens(email))
            .thenReturn(itens);
        
        mockMvc.perform(get("/api/v1/usuario/me/carrinho/itens")
            .contentType(MediaType.APPLICATION_JSON))
            .andExpect(status().isOk())
            .andExpect(jsonPath("$.message").value("Sucesso"))
            .andExpect(jsonPath("$.data").exists());
    }

}
