package com.dailycodework.agroshop.model;

import java.util.UUID;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.OneToOne;
import jakarta.persistence.Table;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Table
@Entity
public class Endereco {
    
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;

    private String endereco;
    private String cidade;
    private String estado;
    private String cep;

    @OneToOne
    @JoinColumn(name="usuario_id")
    private Usuario usuario;
}
