package com.dailycodework.agroshop.model;

import java.math.BigDecimal;
import java.util.List;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Table
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class Produto {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column
    private String nome;

    @Column
    private String marca;

    @Column
    private BigDecimal preco;

    @Column
    private int estoque;

    @Column
    private String descricao ;

    @Column 
    private BigDecimal peso;

    @Column 
    private BigDecimal altura;

    @Column 
    private BigDecimal largura;

    @Column 
    private BigDecimal comprimento;

    @ManyToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "category_id")
    private Categoria categoria;

    @OneToMany(mappedBy = "produto", cascade = CascadeType.ALL, orphanRemoval = true, fetch= FetchType.EAGER)
    private List<Imagem> imagens;

    @OneToMany(mappedBy = "produto", fetch=FetchType.EAGER)
    private List<Avaliacao> avaliacoes;
    
    public Produto(String nome, String marca, BigDecimal preco, 
        int estoque, Categoria categoria, String descricao){
        this.nome = nome;
        this.preco = preco;
        this.marca = marca;
        this.estoque = estoque;
        this.categoria = categoria;
    }
}
