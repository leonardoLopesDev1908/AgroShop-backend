package com.dailycodework.agroshop.service.Product;

import java.math.BigDecimal;
import java.util.List;

import org.springframework.data.domain.Page;

import com.dailycodework.agroshop.controller.dto.register.ProductRegisterDTO;
import com.dailycodework.agroshop.controller.dto.search.ProductSearchDTO;
import com.dailycodework.agroshop.controller.dto.update.ProductUpdateDTO;
import com.dailycodework.agroshop.model.Product;

public interface IProductService {
    
    Product addProduto(ProductRegisterDTO dto);

    Product buscarPorId(Long id);

    Product atualizarProduto(Long id, ProductUpdateDTO dto);

    void deletarProdutoPorId(Long id);

    List<Product> getAllProdutos();

    Page<Product> getProdutos(String search, String categoria, 
                                         BigDecimal precoMin, BigDecimal precoMax,
                                         Integer pagina, Integer tamanhoPagina); 

    List<Product> getProdutoPorMarcaECategoria(String categoria, String marca);
    
    List<Product> getProdutoPorMarcarENome(String marca, String nome);
    
    List<ProductSearchDTO> getProdutoPorNome(String nome);
    
    List<ProductSearchDTO> getProdutoPorMarca(String marca);
    
    List<ProductSearchDTO> getProdutoPorCategoria(String categoria);

    List<Product> findDistinctProdutodsByNome();

    List<ProductSearchDTO> findOutrosProdutos(String categoria);

    Integer getEstoque(Long id);
}
