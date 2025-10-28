package com.dailycodework.agroshop.service.Pedido;

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

import com.dailycodework.agroshop.controller.dto.pesquisa.PedidoPesquisaDTO;
import com.dailycodework.agroshop.controller.mapper.PedidoMapper;
import com.dailycodework.agroshop.model.Carrinho;
import com.dailycodework.agroshop.model.ItemPedido;
import com.dailycodework.agroshop.model.Pedido;
import com.dailycodework.agroshop.model.Produto;
import com.dailycodework.agroshop.model.enums.PedidoStatus;
import com.dailycodework.agroshop.repository.PedidoRepository;
import com.dailycodework.agroshop.repository.PedidoSpecs;
import com.dailycodework.agroshop.repository.ProdutoRepository;
import com.dailycodework.agroshop.service.Carrinho.ICarrinhoService;

import jakarta.persistence.EntityNotFoundException;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class PedidoService implements IPedidoService{

    private final PedidoRepository repository;
    private final ProdutoRepository produtoRepository;
    private final PedidoValidator validator;
    private final ICarrinhoService carrinhoService;
    private final PedidoMapper mapper;
    private final PedidoSpecs specsPedido;

    @Override
    @Transactional
    public PedidoPesquisaDTO fazerPedido(UUID usuarioId) {
        Carrinho carrinho = carrinhoService.buscarPorIdUsuario(usuarioId);
        Pedido pedido = criarPedido(carrinho);
        List<ItemPedido> itens = criarItens(pedido, carrinho);
        //validator.validar(itens);
        pedido.setItens(new HashSet<>(itens));
        pedido.setValorTotal(calcularValorTotal(itens));

        pedido.setData(LocalDateTime.now());
        
        Pedido pedidoSalvo = repository.save(pedido);
        carrinhoService.limparCarrinho(carrinho.getId());

        return mapper.toDTO(pedidoSalvo);
    }

    @Override
    public List<PedidoPesquisaDTO> pedidosUsuario(UUID usuarioId) {
        return repository.findByUsuarioId(usuarioId).stream()   
                .map(mapper::toDTO)
                .toList();
    } 
 
    @Override
    public Page<Pedido> searchPedidos(Long id, String email, LocalDate dataInicio,
                                    LocalDate dataFim, Integer pagina){

        Specification<Pedido> specs = null;

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
    public PedidoPesquisaDTO buscaPedidoPorId(Long id){
        Pedido pedido = repository.findById(id).orElseThrow(() -> {
            throw new EntityNotFoundException("Pedido não encontrado");
        });
        return mapper.toDTO(pedido);
    }

    @Override
    public PedidoPesquisaDTO atualizarPedido(Long id, String novoStatus){
        PedidoStatus status = PedidoStatus.valueOf(novoStatus);
        Pedido pedido = repository.findById(id).orElseThrow(()->{
            throw new EntityNotFoundException("Pedido não encontrado");
        });
        pedido.setStatus(status);
        repository.save(pedido);
        return mapper.toDTO(pedido);
    }

    @Override
    public PedidoPesquisaDTO pedidoCancelar(Long id){
        Pedido pedido = repository.findById(id).orElseThrow(()->{
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

    private BigDecimal calcularValorTotal(List<ItemPedido> itens){
        return itens.stream()
                .map(item -> item.getPreco().multiply(BigDecimal.valueOf(item.getQuantidade())))
                .reduce(BigDecimal.ZERO, BigDecimal::add);
    }

    private List<ItemPedido> criarItens(Pedido pedido, Carrinho carrinho){
        return carrinho.getItems().stream() 
                    .map(item -> {
                        Produto produto = item.getProduto();
                        produto.setEstoque(produto.getEstoque() - item.getQuantidade());
                        produtoRepository.save(produto);
                        return new ItemPedido(
                            pedido,
                            item.getPrecoUnitario(),
                            produto,
                            item.getQuantidade()
                        );
                    }).toList();
    }

    private Pedido criarPedido(Carrinho carrinho){
        Pedido pedido = new Pedido();
        pedido.setUsuario(carrinho.getUsuario());
        pedido.setStatus(PedidoStatus.PENDENTE);
        return pedido;
    }

}
