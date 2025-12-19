package com.dailycodework.agroshop.service.Cart;

import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import org.springframework.stereotype.Service;

import com.dailycodework.agroshop.controller.dto.pesquisa.CartItemSearchDTO;
import com.dailycodework.agroshop.controller.mapper.CartItemMapper;
import com.dailycodework.agroshop.model.Cart;
import com.dailycodework.agroshop.model.User;
import com.dailycodework.agroshop.repository.CarrinhoRepository;
import com.dailycodework.agroshop.repository.ItemCarrinhoRepository;
import com.dailycodework.agroshop.service.User.UserService;

import jakarta.persistence.EntityNotFoundException;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;


@Service
@RequiredArgsConstructor
public class CartService implements ICartService {

    private final CarrinhoRepository repository;
    private final ItemCarrinhoRepository itemRepository;
    private final CartItemMapper mapper;
    private final UserService userService;

    @Override
    public Cart buscarCarrinho(Long id) {
        return repository.findById(id).orElseThrow(() -> {
            throw new EntityNotFoundException("Carrinho não encontrado");
        });
    }

    @Override
    public Cart buscarPorIdUsuario(User user){
        return repository.findByUsuarioId(user.getId());
    }

    @Override
    public Cart buscarPorEmailUsuario(String email) {
        return repository.findByUsuarioEmail(email);
    }   

    @Transactional
    @Override
    public void limparCarrinho(Long id) {
        Cart carrinho = repository.findById(id).orElseThrow(() -> {
            throw new EntityNotFoundException("Carrinho não encontrado");
        });
        itemRepository.deleteAllByCarrinhoId(id);
        carrinho.limpar();
        repository.deleteById(id);
    }

    @Override
    public Cart novoCarro(User usuario) {
        return Optional.ofNullable(buscarPorIdUsuario(usuario)).orElseGet(() -> {
            Cart carrinho = new Cart();
            carrinho.setUsuario(usuario);
            return repository.save(carrinho);
        });
    }

    @Override
    public BigDecimal precoTotal(Long id) {
        Cart carrinho = repository.findById(id).orElseThrow(() -> {
            throw new EntityNotFoundException("Carrinho não encontrado");
        });
        return carrinho.getValorTotal();
    }

    @Override
    public List<CartItemSearchDTO> todosItens(String email){
        User usuario = (userService.buscarPorEmail(email));
        Cart carrinho = usuario.getCarrinho();
        return itemRepository.getAllByCarrinhoId(carrinho.getId()).stream()
                    .map(mapper::toDTO)
                    .collect(Collectors.toList());
    }
    
}
