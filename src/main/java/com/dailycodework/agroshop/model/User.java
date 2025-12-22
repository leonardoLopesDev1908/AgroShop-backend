package com.dailycodework.agroshop.model;

import java.util.Collection;
import java.util.HashSet;
import java.util.List;
import java.util.UUID;

import org.hibernate.annotations.NaturalId;

import com.fasterxml.jackson.annotation.JsonManagedReference;

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
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Table(name= "users")
public class User {
    
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;

    private String nome;
    
    private String sobrenome;

    @NaturalId
    private String email;

    private String senha;

    private String telefone;

    @OneToMany(mappedBy = "user", cascade = CascadeType.ALL, orphanRemoval=true, fetch= FetchType.LAZY)
    @JsonManagedReference
    private List<Address> endereco;

    @OneToMany(mappedBy = "user", fetch = FetchType.LAZY)
    @JsonManagedReference
    private List<Avaliacao> avaliacoes;

    @OneToOne(mappedBy = "user", cascade= CascadeType.ALL, orphanRemoval=true)
    private Cart carrinho;

    @OneToMany(mappedBy = "user", cascade=CascadeType.ALL, orphanRemoval=true)
    private List<Order> pedidos;

    @ManyToMany(fetch = FetchType.EAGER, cascade = 
                    {CascadeType.DETACH, CascadeType.MERGE, CascadeType.PERSIST, CascadeType.REFRESH})
    @JoinTable(name = "user_roles", joinColumns= @JoinColumn(name = "user_id", referencedColumnName = "id"),
                            inverseJoinColumns = @JoinColumn(name = "role_id", referencedColumnName = "id"))
    private Collection<Role> roles = new HashSet<>();

}
