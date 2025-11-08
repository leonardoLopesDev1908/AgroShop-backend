package com.dailycodework.agroshop.service.Usuario;

import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

import com.dailycodework.agroshop.controller.dto.update.UsuarioUpdateDTO;
import com.dailycodework.agroshop.model.Usuario;
import com.dailycodework.agroshop.repository.UsuarioRepository;

import jakarta.persistence.EntityExistsException;
import lombok.RequiredArgsConstructor;

@Component
@RequiredArgsConstructor
public class UsuarioValidator {
    
    private final UsuarioRepository repository;
    private final PasswordEncoder passwordEncoder;

    public void validar(Usuario usuario){
        if (repository.existsByEmail(usuario.getEmail())){
            throw new EntityExistsException("Usuario já cadastrado com o email: " + 
                                                    usuario.getEmail()); 
        }
    }

    public void checkSenha(UsuarioUpdateDTO dto, Usuario usuario){
        if(!passwordEncoder.matches(dto.senhaAtual(), usuario.getSenha())){
            throw new RuntimeException("Senha incorreta");
        }
    }

    public void validarTrocaSenha(Usuario user, String email, String senhaAtual){
        if(!user.getEmail().equals(email) || 
            !passwordEncoder.matches(senhaAtual, user.getSenha())){
                throw new RuntimeException("Dados inválidos");
        }
    }

}
