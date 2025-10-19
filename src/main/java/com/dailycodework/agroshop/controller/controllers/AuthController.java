package com.dailycodework.agroshop.controller.controllers;

import java.util.HashMap;
import java.util.Map;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.dailycodework.agroshop.request.LoginRequest;
import com.dailycodework.agroshop.security.jwt.JwtUtils;
import com.dailycodework.agroshop.security.user.ShopUserDetailsService;
import com.dailycodework.agroshop.utils.CookieUtils;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/api/v1/auth")
@RequiredArgsConstructor
public class AuthController {
    
    private final JwtUtils jwtUtils;
    private final CookieUtils cookieUtils;
    private final ShopUserDetailsService userDetailsService;
    private final AuthenticationManager authenticationManger; 

    @Value("${auth.token.refreshExpirationInMils}")
    private Long refreshTokenExpirationTime;

    @PostMapping("/login")
    public ResponseEntity<?> authenticationUsuario(@RequestBody LoginRequest request, HttpServletResponse response){
        Authentication authentication = authenticationManger
                    .authenticate(new UsernamePasswordAuthenticationToken(request.getEmail(), request.getSenha()));
        String accessToken = jwtUtils.generateAccessToken(authentication);
        String refreshToken = jwtUtils.generateRefreshToken(request.getEmail());
        cookieUtils.addRefreshTokenCookie(response, refreshToken, refreshTokenExpirationTime);        
        
        Map<String, String> token = new HashMap<>();
        token.put("accessToken", accessToken);
        return ResponseEntity.ok(token);
    }

    @PostMapping("/refresh-token")
    public ResponseEntity<?> refreshAccessToken(HttpServletRequest request){
        String refreshToken = cookieUtils.getRefreshTokenFromCookies(request);
        if(refreshToken != null){
            boolean isValid = jwtUtils.validateToken(refreshToken);
            if(isValid){
                String usernameFromToken = jwtUtils.getUsernameDoToken(refreshToken);
                UserDetails userDetails = userDetailsService.loadUserByUsername(usernameFromToken);
                String newAccessToken = jwtUtils.
                    generateAccessToken(new UsernamePasswordAuthenticationToken(userDetails, null, userDetails.getAuthorities()));
                if(newAccessToken != null){
                    Map<String, String> token = new HashMap<>();
                    token.put("accessToken", newAccessToken);
                    return ResponseEntity.ok(token);
                } else {
                    return ResponseEntity.status(500).body("Erro ao gerar novo token de acesso");
                }
            }
        }
        return ResponseEntity.status(HttpStatus.FORBIDDEN).body("Token expirou");
    }

}
