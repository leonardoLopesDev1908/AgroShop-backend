package com.dailycodework.agroshop.service.Category;

import java.util.List;
import java.util.Optional;

import org.springframework.stereotype.Service;

import com.dailycodework.agroshop.controller.dto.register.CategoryRegisterDTO;
import com.dailycodework.agroshop.controller.mapper.CategoryMapper;
import com.dailycodework.agroshop.model.Category;
import com.dailycodework.agroshop.repository.CategoryRepository;

import jakarta.persistence.EntityExistsException;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class CategoryService implements ICategoryService{
    
    private final CategoryRepository repository;
    private final CategoryMapper mapper;

    @Override
    public Category addCategoria(CategoryRegisterDTO dto) {
        Category categoria = mapper.toEntity(dto);
        return Optional.of(categoria).filter(c -> !repository.existsByNome(categoria.getNome()))
            .map(repository::save)
            .orElseThrow(() -> {
                throw new EntityExistsException("Categoria com nome: " + categoria.getNome() + " ja existe");
            });
    }

    @Override
    public Category updateCategoria(CategoryRegisterDTO dto, Long id) {
        Category categoria = mapper.toEntity(dto);
        return Optional.ofNullable(buscaPorId(id))
        .filter(c -> !repository.existsByNomeAndIdNot(categoria.getNome(), id))
        .map(categoriaExistente -> {
            categoriaExistente.setNome(categoria.getNome());
            return repository.save(categoriaExistente);
        }).orElseThrow(() -> {
            throw new EntityNotFoundException("Entidade não encontrada");
        });
    }

    @Override
    public void deleteCategoria(Long id) {
        repository.findById(id).ifPresentOrElse(repository :: delete, () -> {
            throw new EntityNotFoundException("Entidade não encontrada");
        });
    }

    @Override
    public List<Category> getAllCategorias() {
        return repository.findAll();
    }

    @Override
    public Category buscaPorNome(String nome) {
        return repository.findByNome(nome);
    }

    @Override
    public Category buscaPorId(Long id) {
        return repository.findById(id).orElseThrow(() -> {
            throw new EntityNotFoundException("Categoria não encontrada");
        });
    }
}
