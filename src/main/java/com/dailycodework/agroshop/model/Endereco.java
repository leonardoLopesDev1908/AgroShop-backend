package com.dailycodework.agroshop.model;

import java.util.UUID;

import org.hibernate.annotations.NaturalId;

import com.fasterxml.jackson.annotation.JsonIgnore;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Entity
public class Endereco {
    
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;

    private String endereco;
    
    @NaturalId
    private String numero;

    @NaturalId
    private String complemento;
    private String cidade;
    private String estado;

    @NaturalId
    private String cep;

    @ManyToOne
    @JoinColumn(name="usuario_id")
    @JsonIgnore
    private Usuario usuario;
}
