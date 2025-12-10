package com.dailycodework.agroshop.utils;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseCookie;
import org.springframework.stereotype.Component;

import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

@Component
public class CookieUtils{

    @Value("${api.useSecureCookie}")
    private boolean useSecureCookie;
    
    public void addCsrfCookie(HttpServletResponse response, String token){
        ResponseCookie cookie = ResponseCookie.from("XSRF-TOKEN", token)
            .httpOnly(false)
            .secure(useSecureCookie)
            .path("/")
            .sameSite("Lax")
            .build();
        response.addHeader(HttpHeaders.SET_COOKIE, cookie.toString());
    }

    public void addRefreshTokenCookie(HttpServletResponse response, String refreshToken, long maxAge){
        if(response == null){
            throw new IllegalArgumentException("HttpServletResponse cannot be null");
        }
        Cookie refreshTokenCookie = new Cookie("refreshToken", refreshToken);
        refreshTokenCookie.setHttpOnly(true);
        refreshTokenCookie.setPath("/");
        refreshTokenCookie.setMaxAge((int)(maxAge / 1000));
        refreshTokenCookie.setSecure(useSecureCookie);  
        String sameSite = "Lax";
        setResponseHeader(response, refreshTokenCookie, sameSite);
    }

    private void setResponseHeader(HttpServletResponse response, Cookie refreshTokenCookie, String sameSite){
        ResponseCookie cookie = ResponseCookie.from(refreshTokenCookie.getName(), refreshTokenCookie.getValue())
            .httpOnly(true)
            .secure(useSecureCookie)
            .path("/")
            .maxAge(refreshTokenCookie.getMaxAge())
            .sameSite(sameSite)
            .build();
        response.addHeader(HttpHeaders.SET_COOKIE, cookie.toString());
    }

    public String getRefreshTokenFromCookies(HttpServletRequest request){
        Cookie[] cookies = request.getCookies();
        if(cookies != null){
            for(Cookie cookie : cookies){
                System.out.println("Nomes dos cookies encontrados: " + cookie.getName());
                if("refreshToken".equals(cookie.getName())){
                    return cookie.getValue();
                }
            }
        }
        return null;
    }
}