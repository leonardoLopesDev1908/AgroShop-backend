package com.dailycodework.agroshop.security.oauth2;

import java.io.IOException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.Authentication;
import org.springframework.security.web.authentication.SimpleUrlAuthenticationSuccessHandler;
import org.springframework.stereotype.Component;

import com.dailycodework.agroshop.security.jwt.JwtUtils;
import com.dailycodework.agroshop.utils.CookieUtils;

import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

@Component
public class OAuth2LoginSuccessHandler extends SimpleUrlAuthenticationSuccessHandler{
       
    @Autowired
    private JwtUtils jwtUtils;

    @Autowired
    private CookieUtils cookieUtils;

    @Value("${app.oauth2.redirect-url}")
    private String redirectString;

    @Value("${auth.token.accessExpirationInMils}")
    private Long accessTokenExpirationTime;
    
    @Value("${auth.token.refreshExpirationInMils}")
    private Long refreshTokenExpirationTime;

    @Override
    public void onAuthenticationSuccess(HttpServletRequest request,
                                        HttpServletResponse response,
                                        Authentication authentication) 
                                throws IOException, ServletException {
        
        CustomOAuth2User oauth2User = (CustomOAuth2User) authentication.getPrincipal();

        String accessToken = jwtUtils.generateAccessTokenOAuth2(oauth2User);
        String refreshToken = jwtUtils.generateRefreshTokenOAuth2(oauth2User);

        cookieUtils.addAccessTokenCookie(response, accessToken, accessTokenExpirationTime);
        cookieUtils.addRefreshTokenCookie(response, refreshToken, refreshTokenExpirationTime);
        
        getRedirectStrategy().sendRedirect(request, response, redirectString);                            
    }
}
