package com.dailycodework.agroshop.controller;

import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import java.math.BigDecimal;

import com.dailycodework.agroshop.controller.controllers.ProductController;
import com.dailycodework.agroshop.controller.dto.register.ProductRegisterDTO;
import com.dailycodework.agroshop.model.Category;
import com.dailycodework.agroshop.service.Product.IProductService;
import com.fasterxml.jackson.databind.ObjectMapper;

@WebMvcTest(ProductController.class)
public class ProductControllerTest {

    @Autowired
    MockMvc mockMvc;

    @Autowired
    ObjectMapper objectMapper;

    @MockitoBean
    IProductService produtoService;
    
    @Test
    void shouldFindProductById() throws Exception{
        mockMvc.perform(get("/api/v1/produtos/2"))
            .andExpect(status().isOk())
            .andExpect(jsonPath("$.message").value("Sucesso!"))
            .andExpect(jsonPath("$.data").exists());
    }

    @Test
    void shouldSaveProduct() throws Exception {
        Category categoria = new Category("Categoria");
        ProductRegisterDTO produtoCadastro = new ProductRegisterDTO(
            "Teste",
            "Marca",
            BigDecimal.valueOf(1.23),
            2,
            "Descricao", 
            categoria,
            BigDecimal.valueOf(0.8),
            BigDecimal.valueOf(0.5),
            BigDecimal.valueOf(0.2),
            BigDecimal.valueOf(0.4)
        );

        mockMvc.perform(post("/produtos")
            .contentType(MediaType.APPLICATION_JSON)
            .content(objectMapper.writeValueAsString(produtoCadastro)))
            .andExpect(status().isCreated())
            .andExpect(jsonPath("$.message").value("Sucesso!"))
            .andExpect(jsonPath("$.data").exists());
    }

    @Test
    void shouldDeleteProduct() throws Exception{
        mockMvc.perform(delete("/api/v1/produtos/2"))
            .andExpect(status().isNoContent())
            .andExpect(jsonPath("$.message")
                        .value("Produto deletado!"))
            .andExpect(jsonPath("$.data").doesNotExist());
    }

    
    
}