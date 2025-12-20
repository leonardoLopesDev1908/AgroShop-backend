package com.dailycodework.agroshop.repository.specs;

import java.math.BigDecimal;

import org.springframework.data.jpa.domain.Specification;

import com.dailycodework.agroshop.model.Category;
import com.dailycodework.agroshop.model.Product;

import jakarta.persistence.criteria.Path;

public class ProdutosSpecs {

    public static Specification<Product> searchLike(String search){
        return (root, query, cb) -> {
            if(search == null || search.trim().isEmpty()){
                return cb.conjunction();
            }
            return cb.or(
                cb.like(cb.lower(root.get("nome")), "%" + search + "%"),
                cb.like(cb.lower(root.get("descricao")), "%" + search + "%"),
                cb.like(cb.lower(root.get("marca")), "%" + search + "%")
            );
        };
    }

    public static Specification<Product> categoriaEqual(Category categoria){
        return (root, query, cb) -> {
            if(categoria == null){
                return cb.conjunction();
            }

            return cb.equal(root.get("category"), categoria);
        };
    }

    public static Specification<Product> precoBetween(BigDecimal precoMin, BigDecimal precoMax){
        return (root, query, cb) -> {
            if(precoMax == null && precoMin == null){
                return cb.conjunction();
            }
            if(precoMin == null) {
                return cb.lessThanOrEqualTo(root.get("preco"), precoMax);
            }
            if(precoMax == null){
                return cb.greaterThanOrEqualTo(root.get("preco"), precoMin);
            }

            Path<BigDecimal> precoPath = root.get("preco");

            return cb.between(precoPath, precoMin, precoMax);
        };
    }
}
