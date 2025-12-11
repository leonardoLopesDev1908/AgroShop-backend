package com.dailycodework.agroshop.security.oauth2;

import java.util.Collection;
import java.util.Map;

import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.oauth2.core.user.OAuth2User;

import com.dailycodework.agroshop.security.user.ShopUserDetails;

public class CustomOAuth2User implements OAuth2User{

    private final ShopUserDetails user;
    private final Map<String, Object> attributes;

    public CustomOAuth2User(ShopUserDetails user, Map<String, Object> attributes){
        this.user = user;
        this.attributes = attributes;
    }
    
    @Override
    public Map<String, Object> getAttributes() {
        return attributes;
    }

    @Override
    public String getName() {
        return user.getUsername();
    }

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
       return user.getAuthorities();
    }
    
    public String getEmail(){
        return user.getEmail();
    }   

    public String getFirstName(){
        return user.getNome();
    }

    public String getLastName(){
        return user.getSobrenome();
    }
}
