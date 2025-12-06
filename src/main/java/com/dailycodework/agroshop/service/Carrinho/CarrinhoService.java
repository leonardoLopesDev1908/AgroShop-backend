package com.dailycodework.agroshop.service.Carrinho;

import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import org.springframework.stereotype.Service;

import com.dailycodework.agroshop.controller.dto.pesquisa.ItemCarrinhoPesquisaDTO;
import com.dailycodework.agroshop.controller.mapper.ItemCarrinhoMapper;
import com.dailycodework.agroshop.model.Carrinho;
import com.dailycodework.agroshop.model.Usuario;
import com.dailycodework.agroshop.repository.CarrinhoRepository;
import com.dailycodework.agroshop.repository.ItemCarrinhoRepository;
import com.dailycodework.agroshop.service.Usuario.UsuarioService;

import jakarta.persistence.EntityNotFoundException;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;


@Service
@RequiredArgsConstructor
public class CarrinhoService implements ICarrinhoService {

    private final CarrinhoRepository repository;
    private final ItemCarrinhoRepository itemRepository;
    private final ItemCarrinhoMapper mapper;
    private final UsuarioService userService;

    @Override
    public Carrinho buscarCarrinho(Long id) {
        return repository.findById(id).orElseThrow(() -> {
            throw new EntityNotFoundException("Carrinho não encontrado");
        });
    }

    @Override
    public Carrinho buscarPorIdUsuario(Usuario user){
        return repository.findByUsuarioId(user.getId());
    }

    @Override
    public Carrinho buscarPorEmailUsuario(String email) {
        return repository.findByUsuarioEmail(email);
    }   

    @Transactional
    @Override
    public void limparCarrinho(Long id) {
        Carrinho carrinho = repository.findById(id).orElseThrow(() -> {
            throw new EntityNotFoundException("Carrinho não encontrado");
        });
        itemRepository.deleteAllByCarrinhoId(id);
        carrinho.limpar();
        repository.deleteById(id);
    }

    @Override
    public Carrinho novoCarro(Usuario usuario) {
        return Optional.ofNullable(buscarPorIdUsuario(usuario)).orElseGet(() -> {
            Carrinho carrinho = new Carrinho();
            carrinho.setUsuario(usuario);
            return repository.save(carrinho);
        });
    }

    @Override
    public BigDecimal precoTotal(Long id) {
        Carrinho carrinho = repository.findById(id).orElseThrow(() -> {
            throw new EntityNotFoundException("Carrinho não encontrado");
        });
        return carrinho.getValorTotal();
    }

    @Override
    public List<ItemCarrinhoPesquisaDTO> todosItens(String email){
        Usuario usuario = (userService.buscarPorEmail(email));
        Carrinho carrinho = usuario.getCarrinho();
        return itemRepository.getAllByCarrinhoId(carrinho.getId()).stream()
                    .map(mapper::toDTO)
                    .collect(Collectors.toList());
    }
    
}
