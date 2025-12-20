package com.dailycodework.agroshop.model;

import java.math.BigDecimal;
import java.util.HashSet;
import java.util.Set;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
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
@NoArgsConstructor
@AllArgsConstructor
@Table
public class Cart {
    @Id
    @GeneratedValue(strategy=GenerationType.IDENTITY)
    private Long id;

    @Column
    private BigDecimal valorTotal;

    @OneToOne 
    @JoinColumn(name="user_id")
    private User user;

    @OneToMany(mappedBy = "cart", cascade = CascadeType.ALL, orphanRemoval = true)
    private Set<CartItem> items = new HashSet<>();

    public void addItem(CartItem item){
        this.items.add(item);
        item.setCart(this);
        atualizaPreco();
    }

    public void removeItem(CartItem item){
        this.items.remove(item);
        item.setCart(null);
        atualizaPreco();
    }

    public void atualizaPreco(){
        this.valorTotal = BigDecimal.ZERO;
        
        for(CartItem item : items){
            this.valorTotal = this.valorTotal.add(item.getPrecoTotal());
        }
    }

    public void limpar(){
        this.items.clear();
        atualizaPreco();
    }
}
