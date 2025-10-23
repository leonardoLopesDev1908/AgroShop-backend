package com.dailycodework.agroshop.service.Pedido;

import java.util.List;

import org.springframework.stereotype.Component;

import com.dailycodework.agroshop.model.ItemPedido;
import com.dailycodework.agroshop.model.Produto;
import com.dailycodework.agroshop.service.Produto.ProdutoService;

import lombok.RequiredArgsConstructor;

@Component
@RequiredArgsConstructor
public class PedidoValidator {
    
    private final ProdutoService produtoService;
    
    public void validar(List<ItemPedido> itens){
        for(ItemPedido item : itens){
            Produto produto = item.getProduto();
            Integer estoque = produtoService.getEstoque(produto.getId());
            System.out.println("Produto: " + produto.getNome() + " -> " + estoque);
            System.out.println("Quantidade: " + item.getQuantidade());
            // if(item.getQuantidade() > estoque){
            //     throw new IllegalArgumentException("Estoque insuficiente para esse pedido");
            // }
        }
    }

}
