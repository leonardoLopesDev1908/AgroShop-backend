package com.dailycodework.agroshop.service.Order;

import java.util.List;

import org.springframework.stereotype.Component;

import com.dailycodework.agroshop.model.OrderItem;
import com.dailycodework.agroshop.model.Product;
import com.dailycodework.agroshop.service.Product.ProductService;

import lombok.RequiredArgsConstructor;

@Component
@RequiredArgsConstructor
public class OrderAvaliator {
    
    private final ProductService produtoService;
    
    public void validar(List<OrderItem> itens){
        for(OrderItem item : itens){
            Product produto = item.getProduct();
            Integer estoque = produtoService.getEstoque(produto.getId());
            System.out.println("Produto: " + produto.getNome() + " -> " + estoque);
            System.out.println("Quantidade: " + item.getQuantidade());
            if(item.getQuantidade() > estoque){
                throw new IllegalArgumentException("Estoque insuficiente para esse pedido");
            }
        }
    }

}
