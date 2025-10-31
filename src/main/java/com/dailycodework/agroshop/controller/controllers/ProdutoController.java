package com.dailycodework.agroshop.controller.controllers;

import java.math.BigDecimal;
import java.util.List;
import java.util.stream.Collectors;

import org.springframework.data.domain.Page;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.dailycodework.agroshop.controller.dto.cadastro.ProdutoCadastroDTO;
import com.dailycodework.agroshop.controller.dto.pesquisa.ProdutoPesquisaDTO;
import com.dailycodework.agroshop.controller.dto.update.ProdutoUpdateDTO;
import com.dailycodework.agroshop.controller.mapper.ProdutoMapper;
import com.dailycodework.agroshop.model.Produto;
import com.dailycodework.agroshop.response.ApiResponse;
import com.dailycodework.agroshop.service.Produto.IProdutoService;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("${api.prefix}/produtos")
@RequiredArgsConstructor
public class ProdutoController {

    private final IProdutoService service;
    private final ProdutoMapper mapper;

    @GetMapping("/produtos")
    public ResponseEntity<ApiResponse> getAllProdutos(
                                            @RequestParam(value = "search", required=false) String search,
                                            @RequestParam(value = "categoria", required=false) String categoria,
                                            @RequestParam(value = "precoMin", required=false) BigDecimal precoMin,
                                            @RequestParam(value = "precoMax", required=false) BigDecimal precoMax,
                                            @RequestParam(value = "pagina", defaultValue="0") Integer pagina,
                                            @RequestParam(value = "tamanhoPagina", defaultValue="20") Integer tamanhoPagina){
        
        Page<Produto> produtos = service.getProdutos(search, categoria, precoMin, precoMax, pagina, tamanhoPagina);                                            

        List<ProdutoPesquisaDTO> produtoPesquisaDTOs = produtos.getContent().stream()  
                        .map(mapper::toDTO)
                        .collect(Collectors.toList()); 
                                            
        produtoPesquisaDTOs.forEach(System.out::println);

        return ResponseEntity.ok(new ApiResponse("Sucesso!", produtoPesquisaDTOs));
    }

    @GetMapping("/produto/{id}/produto")
    public ResponseEntity<ApiResponse> getProduto(@PathVariable Long id){
        ProdutoPesquisaDTO dto = mapper.toDTO(service.buscarPorId(id));
        return ResponseEntity.ok(new ApiResponse("Sucesso!", dto));
    }

    @GetMapping("/produto/{nome}")
    public ResponseEntity<ApiResponse> getProdutoByNome(@PathVariable String nome){
        List<ProdutoPesquisaDTO> dto = service.getProdutoPorNome(nome).stream() 
                .map(mapper::toDTO)
            .collect(Collectors.toList());
        return ResponseEntity.ok(new ApiResponse("Sucesso!", dto));
    }

    @PostMapping("/cadastrar")
    public ResponseEntity<ApiResponse> cadastroProduto(@Valid @RequestBody ProdutoCadastroDTO dto){
        ProdutoPesquisaDTO produto = mapper.toDTO(service.addProduto(dto));
        return ResponseEntity.ok(new ApiResponse("Sucesso!", produto));
    }

    @DeleteMapping("/produto/deletar/{id}")
    public ResponseEntity<ApiResponse> deletarProduto(@PathVariable Long id){
        service.deletarProdutoPorId(id);
        return ResponseEntity.ok(new ApiResponse("Deletado!", null));
    }   

    @PutMapping("/produto/atualizar/{id}")
    public ResponseEntity<ApiResponse> atualizaProduto(@PathVariable Long id, 
                                                       @RequestBody ProdutoUpdateDTO dto){
        ProdutoPesquisaDTO novoProduto = mapper.toDTO(service.atualizarProduto(id, dto));
        return ResponseEntity.ok(new ApiResponse("Sucesso!", novoProduto));
    }

    @GetMapping("/distintos/produtos")
    public ResponseEntity<ApiResponse> getDistintosPorNome(){
        List<ProdutoPesquisaDTO> produtos = service.findDistinctProdutodsByNome().stream()
                                                .map(mapper::toDTO)
                                                .collect(Collectors.toList());
        return ResponseEntity.ok(new ApiResponse("Sucesso", produtos));
        
    }

}
