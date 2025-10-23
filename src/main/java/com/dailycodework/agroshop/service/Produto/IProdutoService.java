package com.dailycodework.agroshop.service.Produto;

import java.math.BigDecimal;
import java.util.List;

import org.springframework.data.domain.Page;

import com.dailycodework.agroshop.controller.dto.cadastro.ProdutoCadastroDTO;
import com.dailycodework.agroshop.controller.dto.update.ProdutoUpdateDTO;
import com.dailycodework.agroshop.model.Produto;

public interface IProdutoService {
    
    Produto addProduto(ProdutoCadastroDTO dto);

    Produto buscarPorId(Long id);

    Produto atualizarProduto(Long id, ProdutoUpdateDTO dto);

    void deletarProdutoPorId(Long id);

    List<Produto> getAllProdutos();

    Page<Produto> getProdutos(String search, String categoria, 
                                         BigDecimal precoMin, BigDecimal precoMax,
                                         Integer pagina, Integer tamanhoPagina); 

    List<Produto> getProdutoPorMarcaECategoria(String categoria, String marca);
    
    List<Produto> getProdutoPorMarcarENome(String marca, String nome);
    
    List<Produto> getProdutoPorNome(String nome);
    
    List<Produto> getProdutoPorMarca(String marca);
    
    List<Produto> getProdutoPorCategoria(String categoria);

    List<Produto> findDistinctProdutodsByNome();

    Integer getEstoque(Long id);
}
