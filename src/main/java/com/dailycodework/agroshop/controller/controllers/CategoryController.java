package com.dailycodework.agroshop.controller.controllers;

import java.util.List;

import org.springframework.http.HttpStatus;
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

import com.dailycodework.agroshop.controller.dto.register.CategoryRegisterDTO;
import com.dailycodework.agroshop.model.Category;
import com.dailycodework.agroshop.response.ApiResponse;
import com.dailycodework.agroshop.service.Category.ICategoryService;

import jakarta.persistence.EntityExistsException;
import jakarta.persistence.EntityNotFoundException;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;

//Talvez descartar esse Controller

@RestController
@RequiredArgsConstructor
@RequestMapping("${api.prefix}/produtos/categorias")
public class CategoryController {

    private final ICategoryService service;

    @GetMapping("/todas")
    public ResponseEntity<ApiResponse> buscarTodasCategorias(){
        List<Category> categorias = service.getAllCategorias();
        return ResponseEntity.ok(new ApiResponse("Sucesso!", categorias));
    }

    @PostMapping
    public ResponseEntity<ApiResponse> cadastrarCategoria(@Valid @RequestBody CategoryRegisterDTO dto){
        try {
            Category categoriaCriada = service.addCategoria(dto);
            return ResponseEntity.ok(new ApiResponse("Sucesso!", categoriaCriada));
        } catch (EntityExistsException e) {
            return ResponseEntity.status(HttpStatus.CONFLICT)
                .body(new ApiResponse("Erro: ", e.getMessage()));
        }
    }

    @GetMapping("/{id}")
    public ResponseEntity<ApiResponse> buscarCategoria(@PathVariable Long id){
        try {
            Category categoria = service.buscaPorId(id);
            return ResponseEntity.ok(new ApiResponse("Sucesso!", categoria));
        } catch (EntityNotFoundException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                .body(new ApiResponse("Não encontrado", e.getMessage()));
        }
    }

    @GetMapping
    public ResponseEntity<ApiResponse> buscarCategoriaPorNome(@RequestParam String nome){
        try {
            Category categoria = service.buscaPorNome(nome);
            return ResponseEntity.ok(new ApiResponse("Sucesso!", categoria));
        } catch (EntityNotFoundException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                .body(new ApiResponse("Não encontrado", e.getMessage()));
        }
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<ApiResponse> deletarCategoria(@PathVariable Long id){
        try {
            service.deleteCategoria(id);
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(new ApiResponse("Deletado", null));
        } catch (EntityNotFoundException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                .body(new ApiResponse("Não encontrado", e.getMessage()));
        }        
    }

    @PutMapping("/{id}/atualizacao")
    public ResponseEntity<ApiResponse> atualizarCategoria(@RequestBody CategoryRegisterDTO dto
                                                                    , @PathVariable Long id ){
        try {
            Category categoria = service.updateCategoria(dto, id);
            return ResponseEntity.ok(new ApiResponse("Sucesso!", categoria));      
        } catch (EntityNotFoundException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                .body(new ApiResponse("Não encontrado", e.getMessage()));
        }
    }

}
