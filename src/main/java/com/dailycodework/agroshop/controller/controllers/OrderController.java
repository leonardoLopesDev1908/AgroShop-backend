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

import com.dailycodework.agroshop.controller.dto.register.NewOrderRequest;
import com.dailycodework.agroshop.controller.dto.search.CompleteOrderDTO;
import com.dailycodework.agroshop.controller.dto.search.OrderSearchDTO;
import com.dailycodework.agroshop.controller.dto.update.StatusRequest;
import com.dailycodework.agroshop.controller.mapper.OrderMapper;
import com.dailycodework.agroshop.model.Order;
import com.dailycodework.agroshop.model.User;
import com.dailycodework.agroshop.response.ApiResponse;
import com.dailycodework.agroshop.service.Order.IOrderService;
import com.dailycodework.agroshop.service.User.UserService;

import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("${api.prefix}/usuario")
@RequiredArgsConstructor
public class OrderController {
    
    private final IOrderService service;
    private final UserService userService;
    private final OrderMapper mapper;

    @PostMapping("/me/pedido")
    public ResponseEntity<ApiResponse> fazerNovoPedido(@RequestBody NewOrderRequest pedido){
        User usuario = userService.getAuthenticatedUsuario();
        OrderSearchDTO dto = service.fazerPedido(usuario, 
                                    BigDecimal.valueOf((Double.valueOf(pedido.frete().getPrice()))),
                                    pedido.endereco());
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(new ApiResponse("Sucesso", dto));
    }

    @GetMapping("/me/pedidos")
    public ResponseEntity<ApiResponse> buscarPedido(){
        User usuario = userService.getAuthenticatedUsuario();
        List<OrderSearchDTO> lista = service.pedidosUsuario(usuario.getId());
        return ResponseEntity.ok(new ApiResponse("Sucesso", lista));
    }

    @GetMapping("/pedidos")
    public ResponseEntity<ApiResponse> pesquisaPedidos(
                                            @RequestParam(value="id", required=false) Long id,
                                            @RequestParam(value="email", required=false) String email,
                                            @RequestParam(value="dataInicio", required=false) LocalDate dataInicio,
                                            @RequestParam(value="dataFim", required=false) LocalDate dataFim,
                                            @RequestParam(value="pagina", required=false) Integer pagina){

        Page<Order> pedidos = service.searchPedidos(id, email, dataInicio, dataFim, pagina);

        List<OrderSearchDTO> pedidosResponse = pedidos.getContent().stream() 
                                    .map(mapper::toDTO)
                                    .collect(Collectors.toList());
        return ResponseEntity.ok(new ApiResponse("Sucesso!", pedidosResponse));
    }

    @GetMapping("/me/pedido/{id}")
    public ResponseEntity<ApiResponse> buscarPedidosId(@PathVariable Long id){
        OrderSearchDTO pedido = mapper.toDTO(service.buscaPedidoPorId(id));
        return ResponseEntity.ok(new ApiResponse("Sucesso!", pedido));
    }

    @GetMapping("/me/pedido-completo/{id}")
    public ResponseEntity<ApiResponse> buscarPedidoCompleto(@PathVariable Long id){
        CompleteOrderDTO pedido = service.getPedidoCompleto(id);
        System.out.println(pedido.pedido().itens());
        return ResponseEntity.ok(new ApiResponse("Sucesso!", pedido));
    }

    @PutMapping("/pedido/{id}")
    public ResponseEntity<ApiResponse> atualizarPedido(@PathVariable Long id, 
                                                       @RequestBody StatusRequest request){                                                        
        OrderSearchDTO pedido = service.atualizarPedido(id, request.getStatus());
        return ResponseEntity.ok(new ApiResponse("Sucesso!", pedido));
    }

    @PutMapping("/me/pedido/{id}")
    public ResponseEntity<ApiResponse> cancelarPedido(@PathVariable Long id){
        OrderSearchDTO pedido = service.pedidoCancelar(id);
        return ResponseEntity.ok(new ApiResponse("Cancelado com sucesso!", pedido));
    }

    @DeleteMapping("/pedido/{id}")
    public ResponseEntity<ApiResponse> excluirPedido(@PathVariable Long id){
        service.excluirPedido(id);
        return ResponseEntity.status(HttpStatus.NO_CONTENT)
                    .body(new ApiResponse("Pedido deletado!", null));
    }
}
