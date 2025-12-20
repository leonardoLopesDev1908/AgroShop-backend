package com.dailycodework.agroshop.service.Cart;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.dailycodework.agroshop.model.Cart;
import com.dailycodework.agroshop.model.CartItem;
import com.dailycodework.agroshop.model.Product;
import com.dailycodework.agroshop.repository.CartItemRepository;
import com.dailycodework.agroshop.repository.CartRepository;
import com.dailycodework.agroshop.service.Product.IProductService;

import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class CartItemService implements ICartItemService {

    private final CartItemRepository repository;
    private final CartRepository carrinhoRepository;
    
    private final ICartService carrinhoService;
    private final IProductService produtoService;

    @Override
    public void adicionarItem(Long carrinhoId, Long produtoId, int quantidade) {
        Cart carrinho = carrinhoService.buscarCarrinho(carrinhoId);
        Product produto = produtoService.buscarPorId(produtoId);
        CartItem item = carrinho.getItems().stream()    
                    .filter(itemCarrinho -> itemCarrinho.getProduct().getId().equals(produtoId))
                    .findFirst().orElse(new CartItem());
        if(item.getId() == null){
            item.setCart(carrinho);
            item.setProduct(produto);
            item.setQuantidade(quantidade);
        }else{
            item.setQuantidade(item.getQuantidade() + quantidade);
        }
        repository.save(item);
        carrinho.addItem(item);
        carrinho.atualizaPreco();
        carrinhoRepository.save(carrinho);
    }

    @Override
    public void removerItem(Long carrinhoId, Long produtoId) {
        Cart carrinho = carrinhoService.buscarCarrinho(carrinhoId);
        CartItem itemCarrinho = carrinho.getItems().stream()
                    .filter(item -> item.getProduct().getId().equals(produtoId))
                    .findFirst().orElseThrow(() -> new EntityNotFoundException("Item não encontrado"));
        carrinho.removeItem(itemCarrinho);
        carrinhoRepository.save(carrinho);
    }

    @Transactional
    @Override
    public void atualizarQuantidade(Long carrinhoId, Long produtoId, int novaQuantidade) {
        if(novaQuantidade <= 0){
            throw new IllegalArgumentException("Item não pode ter quantidade zero." 
                   +" Deve ser removido do carrinho");
        }
        Cart carrinho = carrinhoService.buscarCarrinho(carrinhoId);
        carrinho.getItems().stream()   
                .filter(item -> item.getProduct().getId().equals(produtoId))
                .findFirst().ifPresent(item -> {
                    item.setQuantidade(novaQuantidade);
                });
        carrinho.atualizaPreco();
    }

    @Override
    public CartItem buscarItens(Long carrinhoId, Long produtoId) {
        Cart carrinho = carrinhoService.buscarCarrinho(produtoId);
        return carrinho.getItems().stream()    
                .filter(item -> item.getProduct().getId().equals(produtoId))
                .findFirst().orElseThrow(()-> new EntityNotFoundException("Item não encontrado"));
    }
    
}
