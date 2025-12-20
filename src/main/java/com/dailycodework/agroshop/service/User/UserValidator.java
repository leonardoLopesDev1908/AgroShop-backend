package com.dailycodework.agroshop.service.User;

import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

import com.dailycodework.agroshop.controller.dto.update.UserUpdateDTO;
import com.dailycodework.agroshop.model.User;
import com.dailycodework.agroshop.repository.UserRepository;

import jakarta.persistence.EntityExistsException;
import lombok.RequiredArgsConstructor;

@Component
@RequiredArgsConstructor
public class UserValidator {
    
    private final UserRepository repository;
    private final PasswordEncoder passwordEncoder;

    public void validar(User usuario){
        if (repository.existsByEmail(usuario.getEmail())){
            throw new EntityExistsException("Usuario já cadastrado com o email: " + 
                                                    usuario.getEmail()); 
        }
    }

    public void checkSenha(UserUpdateDTO dto, User usuario){
        if(!passwordEncoder.matches(dto.senhaAtual(), usuario.getSenha())){
            throw new RuntimeException("Senha incorreta");
        }
    }

    public void validarTrocaSenha(User user, String email, String senhaAtual){
        if(!user.getEmail().equals(email) || 
            !passwordEncoder.matches(senhaAtual, user.getSenha())){
                throw new RuntimeException("Dados inválidos");
        }
    }

}
