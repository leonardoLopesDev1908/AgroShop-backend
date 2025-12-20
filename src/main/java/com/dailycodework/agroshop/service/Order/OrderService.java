package com.dailycodework.agroshop.service.Order;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.HashSet;
import java.util.List;
import java.util.UUID;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;

import com.dailycodework.agroshop.controller.dto.search.AddressSearchDTO;
import com.dailycodework.agroshop.controller.dto.search.CompleteOrderDTO;
import com.dailycodework.agroshop.controller.dto.search.OrderSearchDTO;
import com.dailycodework.agroshop.controller.mapper.AddressMapper;
import com.dailycodework.agroshop.controller.mapper.OrderMapper;
import com.dailycodework.agroshop.model.Address;
import com.dailycodework.agroshop.model.Cart;
import com.dailycodework.agroshop.model.Order;
import com.dailycodework.agroshop.model.OrderItem;
import com.dailycodework.agroshop.model.Product;
import com.dailycodework.agroshop.model.User;
import com.dailycodework.agroshop.model.enums.PedidoStatus;
import com.dailycodework.agroshop.repository.AddressRepository;
import com.dailycodework.agroshop.repository.OrderRepository;
import com.dailycodework.agroshop.repository.ProductRepository;
import com.dailycodework.agroshop.repository.specs.PedidoSpecs;
import com.dailycodework.agroshop.service.Cart.ICartService;

import jakarta.persistence.EntityNotFoundException;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class OrderService implements IOrderService{

    private final OrderRepository repository;
    private final ProductRepository produtoRepository;
    private final AddressRepository enderecoRepository;

    private final OrderAvaliator validator;
    private final ICartService carrinhoService;
    
    private final OrderMapper mapper;
    private final AddressMapper enderecoMapper;
    private final PedidoSpecs specsPedido;

    @Override
    @Transactional
    public OrderSearchDTO fazerPedido(User usuario, BigDecimal frete, AddressSearchDTO enderecoDTO) {
        Cart carrinho = carrinhoService.buscarPorIdUsuario(usuario);
        Order pedido = criarPedido(carrinho);
        List<OrderItem> itens = criarItens(pedido, carrinho);

        Address endereco; 
        
        if(enderecoDTO.complemento() != null && !enderecoDTO.complemento().trim().isEmpty()){
            endereco = enderecoRepository.findByCepAndNumeroAndComplemento(
                    enderecoDTO.cep(), 
                    enderecoDTO.numero(), 
                    enderecoDTO.complemento()).orElseThrow(() -> {
                    throw new EntityNotFoundException("Endereço não encontrado para a combinação: " +
                        enderecoDTO.cep()+", "+ enderecoDTO.numero()+", "+enderecoDTO.complemento()
                    );
                });
        } else {
            endereco = enderecoRepository.findByCepAndNumero(
                    enderecoDTO.cep(), 
                    enderecoDTO.numero()).orElseThrow(() -> {
                    throw new EntityNotFoundException("Endereço não encontrado para a combinação: " +
                        enderecoDTO.cep()+", "+ enderecoDTO.numero()
                    );
                });
        }

        pedido.setEnderecoId(endereco.getId());
        pedido.setItens(new HashSet<>(itens));
        pedido.setValorTotal(calcularValorTotal(itens).add(frete));
        pedido.setFrete(frete);
        pedido.setData(LocalDateTime.now());
        
        Order pedidoSalvo = repository.save(pedido);
        carrinhoService.limparCarrinho(carrinho.getId());

        return mapper.toDTO(pedidoSalvo);
    }

    @Override
    public List<OrderSearchDTO> pedidosUsuario(UUID usuarioId) {
        return repository.findByUserId(usuarioId).stream()   
                .map(mapper::toDTO)
                .toList();
    } 
 
    @Override
    public Page<Order> searchPedidos(Long id, String email, LocalDate dataInicio,
                                    LocalDate dataFim, Integer pagina){

        Specification<Order> specs = null;

        if(id != null){
            specs = (specs == null) ? specsPedido.idEqual(id) : specs.and(specsPedido.idEqual(id));
        }
        if(email != null && !email.isEmpty()){
            specs = (specs == null) ? specsPedido.emailEqual(email) : specs.and(specsPedido.emailEqual(email));
        }
        if(dataInicio != null || dataFim != null){
            specs = (specs == null) ? specsPedido.isDataBetween(dataInicio, dataFim) : 
                                                specs.and(specsPedido.isDataBetween(dataInicio, dataFim));
        }

        Integer tamanhoPagina = 15;

        PageRequest pageRequest = PageRequest.of(pagina, tamanhoPagina);
        return repository.findAll(specs, pageRequest);
    }

    @Override
    public Order buscaPedidoPorId(Long id){
        Order pedido = repository.findById(id).orElseThrow(() -> {
            throw new EntityNotFoundException("Pedido não encontrado");
        });
        return pedido;
    }

    @Override
    public CompleteOrderDTO getPedidoCompleto(Long id){
        Order pedido = repository.findById(id).orElseThrow(() -> {
            throw new EntityNotFoundException("Pedido não encontrado");
        });

        Address endereco = enderecoRepository.findById(pedido.getEnderecoId()).orElseThrow(() -> {
            throw new EntityNotFoundException("Endereço não encontrado");
        });

        return new CompleteOrderDTO(
                mapper.toDTO(pedido),
                enderecoMapper.toDTO(endereco)
        );
    }

    @Override
    public OrderSearchDTO atualizarPedido(Long id, String novoStatus){
        PedidoStatus status = PedidoStatus.valueOf(novoStatus);
        Order pedido = repository.findById(id).orElseThrow(()->{
            throw new EntityNotFoundException("Pedido não encontrado");
        });
        pedido.setStatus(status);
        repository.save(pedido);
        return mapper.toDTO(pedido);
    }

    @Override
    public OrderSearchDTO pedidoCancelar(Long id){
        Order pedido = repository.findById(id).orElseThrow(()->{
            throw new EntityNotFoundException("Pedido não encontrado");
        });
        if(pedido.getStatus().equals(PedidoStatus.ENTREGUE) || 
            pedido.getStatus().equals(PedidoStatus.ENVIADO) ||
            pedido.getStatus().equals(PedidoStatus.CANCELADO)){
                throw new IllegalArgumentException("Pedido não pode mais ser cancelado");
        }
        pedido.setStatus(PedidoStatus.CANCELADO);
        repository.save(pedido);
        return mapper.toDTO(pedido);
    }

    @Override
    public void excluirPedido(Long id){
        repository.deleteById(id);
    }

    private BigDecimal calcularValorTotal(List<OrderItem> itens){
        return itens.stream()
                .map(item -> item.getPreco().multiply(BigDecimal.valueOf(item.getQuantidade())))
                .reduce(BigDecimal.ZERO, BigDecimal::add);
    }

    private List<OrderItem> criarItens(Order pedido, Cart carrinho){
        return carrinho.getItems().stream() 
                    .map(item -> {
                        Product produto = item.getProduct();
                        produto.setEstoque(produto.getEstoque() - item.getQuantidade());
                        produtoRepository.save(produto);
                        return new OrderItem(
                            pedido,
                            item.getPrecoUnitario(),
                            produto,
                            item.getQuantidade()
                        );
                    }).toList();
    }

    private Order criarPedido(Cart carrinho){
        Order pedido = new Order();
        pedido.setUser(carrinho.getUser());
        pedido.setStatus(PedidoStatus.PENDENTE);
        return pedido;
    }

}
