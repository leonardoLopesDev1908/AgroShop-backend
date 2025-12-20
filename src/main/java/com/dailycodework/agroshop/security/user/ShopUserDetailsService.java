package com.dailycodework.agroshop.security.user;

import java.util.Optional;

import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import com.dailycodework.agroshop.model.User;
import com.dailycodework.agroshop.repository.UserRepository;

import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class ShopUserDetailsService implements UserDetailsService {
    
    private final UserRepository repository;

    @Override
    public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {
        User usuario = Optional.ofNullable(repository.findByEmail(email))
                .orElseThrow(() -> new EntityNotFoundException("Usuário não encontrado!"));
            
        return ShopUserDetails.buildUserDetails(usuario);
    }
    
}
