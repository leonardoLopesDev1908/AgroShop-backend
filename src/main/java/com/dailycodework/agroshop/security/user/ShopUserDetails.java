package com.dailycodework.agroshop.security.user;

import java.util.Collection;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import com.dailycodework.agroshop.model.User;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;


@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class ShopUserDetails implements UserDetails{

    private UUID id;
    private String email;
    private String senha;
    private String nome;
    private String sobrenome;
    private String telefone;

    private Collection<GrantedAuthority> authorities;

    public static ShopUserDetails buildUserDetails(User usuario){
        List<GrantedAuthority> authorities = usuario.getRoles().stream()
                .map(role -> new SimpleGrantedAuthority(role.getNome()))
                .collect(Collectors.toList());
            
        return new ShopUserDetails(
            usuario.getId(),
            usuario.getEmail(),
            usuario.getSenha(),
            usuario.getNome(),
            usuario.getSobrenome(),
            usuario.getTelefone(),
            authorities
        );
    }

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return authorities;
    }

    @Override
    public String getPassword() {
        return senha;
    }

    @Override
    public String getUsername() {
        return email;
    }  
    
    public String getNome(){
        return nome;
    }

    public String getSobrenome(){
        return sobrenome;
    }

    public String getTelefone(){
        return telefone;
    }
}
