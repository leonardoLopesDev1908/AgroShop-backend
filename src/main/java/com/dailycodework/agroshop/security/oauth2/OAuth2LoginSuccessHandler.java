package com.dailycodework.agroshop.security.oauth2;

import java.io.IOException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.Authentication;
import org.springframework.security.web.authentication.SimpleUrlAuthenticationSuccessHandler;
import org.springframework.stereotype.Component;

import com.dailycodework.agroshop.security.jwt.JwtUtils;

import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

@Component
public class OAuth2LoginSuccessHandler extends SimpleUrlAuthenticationSuccessHandler{
    
    @Autowired
    private JwtUtils jwtUtils;

    @Value("${app.oauth2.redirect-url}")
    private String redirectString;

    @Override
    public void onAuthenticationSuccess(HttpServletRequest request,
                                        HttpServletResponse response,
                                        Authentication authentication) 
                                throws IOException, ServletException {
        
        System.out.println("Redirect URL: " + redirectString);

        CustomOAuth2User oauth2User = (CustomOAuth2User) authentication.getPrincipal();

        String token = jwtUtils.generateJwtTokenOAuth2(oauth2User);

        String fullRedirectUrl = redirectString + "?token=" + token;
        
        getRedirectStrategy().sendRedirect(request, response, fullRedirectUrl);                            
    }
}
