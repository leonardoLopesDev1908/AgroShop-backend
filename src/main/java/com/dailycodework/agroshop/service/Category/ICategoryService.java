package com.dailycodework.agroshop.service.Category;

import java.util.List;

import com.dailycodework.agroshop.controller.dto.register.CategoryRegisterDTO;
import com.dailycodework.agroshop.model.Category;

public interface ICategoryService {
    
    Category addCategoria(CategoryRegisterDTO dto);
    Category updateCategoria(CategoryRegisterDTO dto, Long id);
    void deleteCategoria(Long id);
    List<Category> getAllCategorias();
    Category buscaPorNome(String nome);
    Category buscaPorId(Long id);
}
