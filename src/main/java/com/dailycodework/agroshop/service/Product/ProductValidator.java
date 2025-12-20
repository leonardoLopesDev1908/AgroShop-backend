package com.dailycodework.agroshop.service.Product;

import java.math.BigDecimal;

import org.springframework.stereotype.Component;

import com.dailycodework.agroshop.model.Product;
import com.dailycodework.agroshop.repository.ProductRepository;

import jakarta.persistence.EntityExistsException;
import lombok.RequiredArgsConstructor;

@Component
@RequiredArgsConstructor
public class ProductValidator {
    
    private final ProductRepository repository;

    public void validarCriacaoProduto(Product produto){ 
        validaUnicidade(produto, null);
    }

    public void validarAtualizacaoProduto(Product produto, Long id){
        validaPreco(produto);
        validaUnicidade(produto, id);
    }

    private void validaPreco(Product produto){
        if(produto.getPreco() != null && produto.getPreco().compareTo(BigDecimal.ZERO) <= 0){
            throw new IllegalArgumentException("Preço inválido");
        }
    }

    private void validaUnicidade(Product produto, Long id){
        boolean existeDuplicado = (id != null) ? 
                    repository.existsByNomeAndMarcaAndIdNot(produto.getNome(), produto.getMarca(), id) :
                    repository.existsByNomeAndMarca(produto.getNome(), produto.getMarca());

        if(existeDuplicado){
            throw new EntityExistsException("Produto ja cadastrado");
        }
    }
}
