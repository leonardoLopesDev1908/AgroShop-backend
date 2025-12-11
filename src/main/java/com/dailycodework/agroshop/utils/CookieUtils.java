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

    public static final String ACCESS_TOKEN_COOKIE = "accessToken";
    public static final String REFRESH_TOKEN_COOKIE = "refreshToken";

    public void addAccessTokenCookie(HttpServletResponse response, String accessToken,
                                        long maxAge){
        addTokenCookie(response, ACCESS_TOKEN_COOKIE, accessToken, maxAge, "/");
    }

    public void addRefreshTokenCookie(HttpServletResponse response, String refreshToken, long maxAge){
        addTokenCookie(response, REFRESH_TOKEN_COOKIE, refreshToken, maxAge, "/");
    }

    private void addTokenCookie(HttpServletResponse response, String name, String token,
                                long maxAge, String path){

        ResponseCookie cookie = ResponseCookie.from(name, token)
            .httpOnly(true)
            .secure(true)
            .path(path)
            .maxAge(maxAge / 1000)
            .sameSite("Strict")
            .build();

        response.addHeader(HttpHeaders.SET_COOKIE, cookie.toString());
    }

    public String getAccessTokenFromCookies(HttpServletRequest request){
        Cookie[] cookies = request.getCookies();
        if(cookies != null){
            for(Cookie cookie : cookies){
                if(ACCESS_TOKEN_COOKIE.equals(cookie.getName())){
                    return cookie.getValue();
                }
            }
        }
        return null;
    }

    public String getRefreshTokenFromCookies(HttpServletRequest request){
        Cookie[] cookies = request.getCookies();
        if(cookies != null){
            for(Cookie cookie : cookies){
                if(REFRESH_TOKEN_COOKIE.equals(cookie.getName())){
                    return cookie.getValue();
                }
            }
        }
        return null;
    }

    public void clearTokens(HttpServletResponse response){
        ResponseCookie accessCookie = ResponseCookie.from(ACCESS_TOKEN_COOKIE, "")
            .httpOnly(true)
            .secure(true)
            .path("/")
            .maxAge(0)
            .sameSite("Strict")
            .build();

        ResponseCookie refreshCookie = ResponseCookie.from(REFRESH_TOKEN_COOKIE, "")
            .httpOnly(true)
            .secure(true)
            .path("/")
            .maxAge(0)
            .sameSite("Strict")
            .build();

        response.addHeader(HttpHeaders.SET_COOKIE, accessCookie.toString());
        response.addHeader(HttpHeaders.SET_COOKIE, refreshCookie.toString());
    }
}