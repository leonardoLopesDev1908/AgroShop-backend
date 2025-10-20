package com.dailycodework.agroshop.controller.controllers;

import java.util.List;
import java.util.UUID;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.dailycodework.agroshop.controller.dto.cadastro.PedidoCadastroDTO;
import com.dailycodework.agroshop.model.Usuario;
import com.dailycodework.agroshop.response.ApiResponse;
import com.dailycodework.agroshop.service.Pedido.IPedidoService;
import com.dailycodework.agroshop.service.Usuario.UsuarioService;

import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("${api.prefix}/pedidos")
@RequiredArgsConstructor
public class PedidoController {
    
    private final IPedidoService service;
    private final UsuarioService userService;

    @PostMapping("/usuario/pedido")
    public ResponseEntity<ApiResponse> fazerNovoPedido(){
        Usuario usuario = userService.getAuthenticatedUsuario();
        PedidoCadastroDTO dto = service.fazerPedido(usuario.getId());
        return ResponseEntity.ok(new ApiResponse("Sucesso", dto));
    }

    @GetMapping("/usuario/{id}/pedidos")
    public ResponseEntity<ApiResponse> buscarPedido(@PathVariable UUID id){
        List<PedidoCadastroDTO> lista = service.pedidosUsuario(id);
        return ResponseEntity.ok(new ApiResponse("Sucesso", lista));
    }
}
