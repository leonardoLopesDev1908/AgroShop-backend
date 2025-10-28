package com.dailycodework.agroshop.repository;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.List;

import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Component;

import com.dailycodework.agroshop.model.Pedido;
import com.dailycodework.agroshop.model.Usuario;

import jakarta.persistence.criteria.Join;
import jakarta.persistence.criteria.JoinType;
import jakarta.persistence.criteria.Predicate;
import lombok.RequiredArgsConstructor;

@Component
@RequiredArgsConstructor
public class PedidoSpecs {

    public Specification<Pedido> idEqual(Long id){
        return(root, query, cb) -> {
            if(id == null){
                return cb.conjunction();
            }
            return cb.equal(root.get("id"), "id");
        };
    }

    public Specification<Pedido> emailEqual(String email){
        return (root, query, cb) -> {
            if(email == null || email.trim().isEmpty()){
                return cb.conjunction();
            }
            Join<Pedido, Usuario> usuarioJoin = root.join("usuario", JoinType.INNER);
            
            return cb.equal(usuarioJoin.get("email"), email);
        };
    }

    public Specification<Pedido> isDataBetween(LocalDate dataInicio, LocalDate dataFim){
        return (root, query, cb) -> {
            if(dataInicio == null && dataFim == null){
                return cb.conjunction();
            }
            List<Predicate> predicates = new ArrayList<>();
            if(dataInicio != null){
                LocalDateTime startTime = dataInicio.atStartOfDay();
                predicates.add(cb.greaterThanOrEqualTo(root.get("data"), startTime));
            }
            if(dataFim != null){
                LocalDateTime endDate = dataFim.atTime(LocalTime.MAX);
                predicates.add(cb.lessThanOrEqualTo(root.get("data"), endDate));
            }

            return cb.and(predicates.toArray(new Predicate[0]));
        };
    }
}
