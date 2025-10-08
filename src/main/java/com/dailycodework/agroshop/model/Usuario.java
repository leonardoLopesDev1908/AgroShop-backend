package com.dailycodework.agroshop.model;

import java.util.Collection;
import java.util.HashSet;
import java.util.List;
import java.util.UUID;

import org.hibernate.annotations.NaturalId;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.JoinTable;
import jakarta.persistence.ManyToMany;
import jakarta.persistence.OneToMany;
import jakarta.persistence.OneToOne;
import jakarta.persistence.Table;
import lombok.Data;

@Entity
@Data
@Table
public class Usuario {
    
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;

    private String nome;
    
    private String sobrenome;

    @NaturalId
    private String email;

    private String senha;

    @OneToOne(mappedBy = "usuario", cascade = CascadeType.ALL, orphanRemoval=true)
    private Endereco endereco;

    @OneToOne(mappedBy = "usuario", cascade= CascadeType.ALL, orphanRemoval=true)
    private Carrinho carrinho;

    @OneToMany(mappedBy = "usuario", cascade=CascadeType.ALL, orphanRemoval=true)
    private List<Pedido> pedidos;

    @ManyToMany(fetch = FetchType.EAGER, cascade = 
                    {CascadeType.DETACH, CascadeType.MERGE, CascadeType.PERSIST, CascadeType.REFRESH})
    @JoinTable(name = "usuario_roles", joinColumns= @JoinColumn(name = "usuario_id", referencedColumnName = "id"),
                            inverseJoinColumns = @JoinColumn(name = "role_id", referencedColumnName = "id"))
    private Collection<Role> roles = new HashSet<>();
}
