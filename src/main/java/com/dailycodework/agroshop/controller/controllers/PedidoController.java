package com.dailycodework.agroshop.controller.controllers;

import java.util.List;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.dailycodework.agroshop.controller.dto.pesquisa.PedidoPesquisaDTO;
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

    @PostMapping("/usuario/solicitar")
    public ResponseEntity<ApiResponse> fazerNovoPedido(){
        Usuario usuario = userService.getAuthenticatedUsuario();
        PedidoPesquisaDTO dto = service.fazerPedido(usuario.getId());
        return ResponseEntity.ok(new ApiResponse("Sucesso", dto));
    }

    @GetMapping("/usuario/pedidos")
    public ResponseEntity<ApiResponse> buscarPedido(){
        Usuario usuario = userService.getAuthenticatedUsuario();
        List<PedidoPesquisaDTO> lista = service.pedidosUsuario(usuario.getId());
        return ResponseEntity.ok(new ApiResponse("Sucesso", lista));
    }

    // @PutMapping("/pedido/{id}/atualizar")
    // public ResponseEntity<ApiResponse> atualizarPedido(@PathVariable UUID id, 
    //                                                    @RequestBody String novoStatus){
                                                    
    // }

    // @PutMapping("/pedido/{id}/cancelar")
    // public ResponseEntity<ApiResponse> cancelarPedido(@PathVariable UUID id){

    // }
}
