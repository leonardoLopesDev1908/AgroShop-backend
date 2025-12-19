package com.dailycodework.agroshop.controller;

import java.math.BigDecimal;
import java.util.List;

import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import com.dailycodework.agroshop.controller.dto.pesquisa.AddressSearchDTO;
import com.dailycodework.agroshop.controller.dto.pesquisa.FreteDTO;
import com.dailycodework.agroshop.controller.dto.pesquisa.FreteDTO.Company;
import com.dailycodework.agroshop.controller.dto.pesquisa.OrderSearchDTO;
import com.dailycodework.agroshop.controller.dto.update.StatusRequest;
import com.dailycodework.agroshop.model.Order;
import com.dailycodework.agroshop.model.User;
import com.dailycodework.agroshop.service.Order.IOrderService;
import com.dailycodework.agroshop.service.User.UserService;

@WebMvcTest
public class OrderControllerTest {
    
    @Autowired
    private UserService usuarioService;

    @MockitoBean
    private IOrderService pedidoService;

    @MockitoBean
    private MockMvc mockMvc;

    @Test
    @WithMockUser(username = "teste@gmail.com") 
    void deveFazerNovoPedido() throws Exception {
        User usuario = new User();
        FreteDTO dto = new FreteDTO();
        dto.setId(0);
        dto.setName("Teste");
        dto.setPrice("5");
        dto.setDelivery_time(0);
        dto.setCompany(new Company()); 

        BigDecimal preco = BigDecimal.valueOf(Double.valueOf(dto.price));

        OrderSearchDTO pedido = null;

        AddressSearchDTO endereco = new AddressSearchDTO(
            "a",
             "123",
            "ap 101" ,
            "Cidade",
            "AB",
             "1234");

        Mockito.when(usuarioService.getAuthenticatedUsuario())
            .thenReturn(usuario);
        
        Mockito.when(pedidoService.fazerPedido(usuario, preco, endereco))
            .thenReturn(pedido);

        mockMvc.perform(post("/api/v1/usuario/me/pedido")
            .contentType(MediaType.APPLICATION_JSON))
            .andExpect(status().isOk())
            .andExpect(jsonPath("$.message").value("Sucesso!"))
            .andExpect(jsonPath("$.data").exists());        
    }

    @Test
    @WithMockUser(username = "teste@gmail.com")
    void deveBuscarPedidos() throws Exception {
        User usuario = new User();
        List<OrderSearchDTO> pedidos = null;
        
        Mockito.when(usuarioService.getAuthenticatedUsuario())
            .thenReturn(usuario);
        
        Mockito.when(pedidoService.pedidosUsuario(usuario.getId()))
            .thenReturn(pedidos);
        
        mockMvc.perform(get("/api/v1/usuario/me/pedidos")
            .contentType(MediaType.APPLICATION_JSON))
            .andExpect(status().isOk())
            .andExpect(jsonPath("$.message").value("Sucesso!"))
            .andExpect(jsonPath("$.data").exists());
    }

    @Test
    @WithMockUser(username = "teste@gmail.com")
    void deveBuscarPedidoPorId() throws Exception {
        User usuario = new User();
        Order pedido = null;

        Mockito.when(usuarioService.getAuthenticatedUsuario())
            .thenReturn(usuario);
        
        Mockito.when(pedidoService.buscaPedidoPorId(Long.valueOf("0")))
            .thenReturn(pedido);
        
        mockMvc.perform(get("/api/v1/usuario/me/pedido/0")
            .contentType(MediaType.APPLICATION_JSON))
            .andExpect(status().isOk())
            .andExpect(jsonPath("$.message").value("Sucesso!"))
            .andExpect(jsonPath("$.data").exists());
    }

    @Test
    @WithMockUser(username = "teste@gmail.com")
    void deveAtualizarPedido() throws Exception {
        OrderSearchDTO dto = null;
        StatusRequest status = new StatusRequest();
        status.setStatus("ENVIADO");

        Mockito.when(pedidoService.atualizarPedido(Long.valueOf(0), status.getStatus()))
            .thenReturn(dto);
        
        mockMvc.perform(put("/api/v1/usuario/pedido/0/atualizar")
            .contentType(MediaType.APPLICATION_JSON))
            .andExpect(status().isOk())
            .andExpect(jsonPath("$.message").value("Sucesso!"))
            .andExpect(jsonPath("$.data").exists());
    }
}
