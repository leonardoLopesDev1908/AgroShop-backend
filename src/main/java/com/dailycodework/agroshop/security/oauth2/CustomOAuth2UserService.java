package com.dailycodework.agroshop.security.oauth2;

import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.oauth2.client.userinfo.DefaultOAuth2UserService;
import org.springframework.security.oauth2.client.userinfo.OAuth2UserRequest;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.stereotype.Service;

import com.dailycodework.agroshop.model.User;
import com.dailycodework.agroshop.repository.UsuarioRepository;
import com.dailycodework.agroshop.security.user.ShopUserDetails;

@Service
public class CustomOAuth2UserService extends DefaultOAuth2UserService{
 
    @Autowired
    private UsuarioRepository userRepository;

    @Override
    public OAuth2User loadUser(OAuth2UserRequest userRequest){
        
        OAuth2User oauth2User = super.loadUser(userRequest);

        String email = oauth2User.getAttribute("email");
        String name = oauth2User.getAttribute("name");

        User usuario = Optional.ofNullable(userRepository.findByEmail(email))
                            .orElseGet(() -> {
                                User novoUsuario = new User();
                                novoUsuario.setEmail(email);
                                novoUsuario.setNome(name);
                                return userRepository.save(novoUsuario);
                            });

        ShopUserDetails shopUser = ShopUserDetails.buildUserDetails(usuario);
                            
        return new CustomOAuth2User(shopUser, oauth2User.getAttributes());
    }

}
