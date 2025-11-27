package com.dailycodework.agroshop.security.oauth2;

import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.oauth2.client.userinfo.DefaultOAuth2UserService;
import org.springframework.security.oauth2.client.userinfo.OAuth2UserRequest;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.stereotype.Service;

import com.dailycodework.agroshop.model.Usuario;
import com.dailycodework.agroshop.repository.UsuarioRepository;

@Service
public class CustomOAuth2UserService extends DefaultOAuth2UserService{
 
    @Autowired
    private UsuarioRepository userRepository;

    @Override
    public OAuth2User loadUser(OAuth2UserRequest userRequest){
        
        OAuth2User oauth2User = super.loadUser(userRequest);

        CustomOAuth2User customUser = new CustomOAuth2User(oauth2User);

        String email = customUser.getEmail();
        String name = customUser.getFirstName();
        String lastName = customUser.getLastName();

        Usuario usuario = Optional.ofNullable(userRepository.findByEmail(email))
                            .orElseGet(() -> {
                                Usuario novoUsuario = new Usuario();
                                novoUsuario.setEmail(email);
                                novoUsuario.setNome(name);
                                novoUsuario.setSobrenome(lastName);
                                return userRepository.save(novoUsuario);
                            });

        userRepository.save(usuario);
        return customUser;
    }

}
