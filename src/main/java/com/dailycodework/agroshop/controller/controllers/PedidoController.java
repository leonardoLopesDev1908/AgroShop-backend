package com.dailycodework.agroshop.controller.controllers;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;
import java.util.stream.Collectors;

import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.dailycodework.agroshop.controller.dto.pesquisa.FreteDTO;
import com.dailycodework.agroshop.controller.dto.pesquisa.PedidoPesquisaDTO;
import com.dailycodework.agroshop.controller.dto.update.StatusRequest;
import com.dailycodework.agroshop.controller.mapper.PedidoMapper;
import com.dailycodework.agroshop.model.Pedido;
import com.dailycodework.agroshop.model.Usuario;
import com.dailycodework.agroshop.response.ApiResponse;
import com.dailycodework.agroshop.service.Pedido.IPedidoService;
import com.dailycodework.agroshop.service.Usuario.UsuarioService;

import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("${api.prefix}/usuario")
@RequiredArgsConstructor
public class PedidoController {
    
    private final IPedidoService service;
    private final UsuarioService userService;
    private final PedidoMapper mapper;

    @PostMapping("/me/pedido")
    public ResponseEntity<ApiResponse> fazerNovoPedido(@RequestBody FreteDTO frete){
        Usuario usuario = userService.getAuthenticatedUsuario();
        PedidoPesquisaDTO dto = service.fazerPedido(usuario, BigDecimal.valueOf((Double.valueOf(frete.getPrice()))));
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(new ApiResponse("Sucesso", dto));
    }

    @GetMapping("/me/pedidos")
    public ResponseEntity<ApiResponse> buscarPedido(){
        Usuario usuario = userService.getAuthenticatedUsuario();
        List<PedidoPesquisaDTO> lista = service.pedidosUsuario(usuario.getId());
        return ResponseEntity.ok(new ApiResponse("Sucesso", lista));
    }

    @GetMapping("/pedidos")
    public ResponseEntity<ApiResponse> pesquisaPedidos(
                                            @RequestParam(value="id", required=false) Long id,
                                            @RequestParam(value="email", required=false) String email,
                                            @RequestParam(value="dataInicio", required=false) LocalDate dataInicio,
                                            @RequestParam(value="dataFim", required=false) LocalDate dataFim,
                                            @RequestParam(value="pagina", required=false) Integer pagina){

        Page<Pedido> pedidos = service.searchPedidos(id, email, dataInicio, dataFim, pagina);

        List<PedidoPesquisaDTO> pedidosResponse = pedidos.getContent().stream() 
                                    .map(mapper::toDTO)
                                    .collect(Collectors.toList());
        return ResponseEntity.ok(new ApiResponse("Sucesso!", pedidosResponse));
    }

    @GetMapping("/me/pedido/{id}")
    public ResponseEntity<ApiResponse> buscarPedidosId(@PathVariable Long id){
        PedidoPesquisaDTO pedido = service.buscaPedidoPorId(id);
        return ResponseEntity.ok(new ApiResponse("Sucesso!", pedido));
    }

    @PutMapping("/pedido/{id}")
    public ResponseEntity<ApiResponse> atualizarPedido(@PathVariable Long id, 
                                                       @RequestBody StatusRequest request){                                                        
        PedidoPesquisaDTO pedido = service.atualizarPedido(id, request.getStatus());
        return ResponseEntity.ok(new ApiResponse("Sucesso!", pedido));
    }

    @PutMapping("/me/pedido/{id}")
    public ResponseEntity<ApiResponse> cancelarPedido(@PathVariable Long id){
        PedidoPesquisaDTO pedido = service.pedidoCancelar(id);
        return ResponseEntity.ok(new ApiResponse("Cancelado com sucesso!", pedido));
    }

    @DeleteMapping("/pedido/{id}")
    public ResponseEntity<ApiResponse> excluirPedido(@PathVariable Long id){
        service.excluirPedido(id);
        return ResponseEntity.status(HttpStatus.NO_CONTENT)
                    .body(new ApiResponse("Pedido deletado!", null));
    }
}
