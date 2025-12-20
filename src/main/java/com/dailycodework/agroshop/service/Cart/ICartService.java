package com.dailycodework.agroshop.service.Cart;

import java.math.BigDecimal;
import java.util.List;

import com.dailycodework.agroshop.controller.dto.search.CartItemSearchDTO;
import com.dailycodework.agroshop.model.Cart;
import com.dailycodework.agroshop.model.User;

public interface ICartService {
    Cart buscarCarrinho(Long id);
    Cart buscarPorIdUsuario(User user);
    Cart buscarPorEmailUsuario(String email);    
    void limparCarrinho(Long id);
    Cart novoCarro(User usuario);    
    BigDecimal precoTotal(Long id);
    List<CartItemSearchDTO> todosItens(String email);
}
