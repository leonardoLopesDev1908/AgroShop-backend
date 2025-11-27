package com.dailycodework.agroshop.security.oauth2;

import java.util.Collection;
import java.util.Map;

import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.oauth2.core.user.OAuth2User;

public class CustomOAuth2User implements OAuth2User{

    private final OAuth2User oauth2User;

    public CustomOAuth2User(OAuth2User oauth2User){
        this.oauth2User = oauth2User;
    }
    
    @Override
    public Map<String, Object> getAttributes() {
        return oauth2User.getAttributes();
    }

    @Override
    public String getName() {
        return oauth2User.getAttribute("sub");
    }

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
       return oauth2User.getAuthorities();
    }
    
    public String getEmail(){
        return oauth2User.getAttribute("email");
    }   

    public String getFirstName(){
        return oauth2User.getAttribute("name");
    }

    public String getLastName(){
        return oauth2User.getAttribute("lastname");
    }
}
