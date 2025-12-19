package com.dailycodework.agroshop.service.Cart;

import com.dailycodework.agroshop.model.CartItem;

public interface ICartItemService {
    void adicionarItem(Long carrinhoId, Long produtoId, int quantidade);
    void removerItem(Long carrinhoId, Long produtoId);
    void atualizarQuantidade(Long carrinhoId, Long produtoId, int novaQuantidade);
    CartItem buscarItens(Long carrinhoId, Long produtoId);
}
