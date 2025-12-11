package com.dailycodework.agroshop.controller.controllers;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.CookieValue;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.dailycodework.agroshop.request.LoginRequest;
import com.dailycodework.agroshop.security.jwt.JwtUtils;
import com.dailycodework.agroshop.security.oauth2.CustomOAuth2UserService;
import com.dailycodework.agroshop.security.user.ShopUserDetails;
import com.dailycodework.agroshop.security.user.ShopUserDetailsService;
import com.dailycodework.agroshop.utils.CookieUtils;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/api/v1/auth")
@RequiredArgsConstructor
@CrossOrigin(origins = "http://localhost:5174", allowCredentials = "true")
public class AuthController {
    
    private final JwtUtils jwtUtils;
    private final CookieUtils cookieUtils;
    private final ShopUserDetailsService userDetailsService;
    private final AuthenticationManager authenticationManger; 
    private final CustomOAuth2UserService oAuth2UserService;

    @Value("${auth.token.accessExpirationInMils}")
    private Long accessTokenExpirationTime;
    
    @Value("${auth.token.refreshExpirationInMils}")
    private Long refreshTokenExpirationTime;

    @PostMapping("/login")
    public ResponseEntity<?> authenticationUsuario(@RequestBody LoginRequest request, HttpServletResponse response){
      
        Authentication authentication = authenticationManger
                    .authenticate(new UsernamePasswordAuthenticationToken(request.getEmail(), request.getSenha()));

        String accessToken = jwtUtils.generateAccessToken(authentication);
        String refreshToken = jwtUtils.generateRefreshToken(request.getEmail());
      
        cookieUtils.addAccessTokenCookie(response, accessToken, accessTokenExpirationTime);
        cookieUtils.addRefreshTokenCookie(response, refreshToken, refreshTokenExpirationTime);        
        
        ShopUserDetails userDetails = (ShopUserDetails) authentication.getPrincipal();

        List<String> roles = userDetails.getAuthorities()
                                .stream()
                                .map(role -> role.toString())
                                .collect(Collectors.toList());

        Map<String, Object> responseBody = new HashMap<>();
        responseBody.put("email", userDetails.getEmail());
        responseBody.put("roles", roles);
        responseBody.put("id", userDetails.getId());
        responseBody.put("nome", userDetails.getNome());
        responseBody.put("sobrenome", userDetails.getSobrenome());
        responseBody.put("telefone", userDetails.getTelefone());

        return ResponseEntity.ok(responseBody);
    }

    @PostMapping("/refresh-token")
    public ResponseEntity<?> refreshAccessToken(HttpServletRequest request, HttpServletResponse response){

        String refreshToken = cookieUtils.getRefreshTokenFromCookies(request);

        if(!jwtUtils.isRefreshToken(refreshToken)){
            return ResponseEntity.status(HttpStatus.FORBIDDEN)
                .body("Refresh token inválido ou não fornecido");
        }

        try{
            String usernameFromToken = jwtUtils.getUsernameDoToken(refreshToken);
            UserDetails userDetails = userDetailsService.loadUserByUsername(usernameFromToken);

            Authentication authentication = new UsernamePasswordAuthenticationToken(
                    userDetails, null, userDetails.getAuthorities()
            );

            String newAccessToken = jwtUtils.generateAccessToken(authentication);

            cookieUtils.addAccessTokenCookie(response, newAccessToken, accessTokenExpirationTime);
            
            return ResponseEntity.ok().body(Map.of(
                "message","Token atualizado com sucesso"
            ));
        } catch(Exception e){
            cookieUtils.clearTokens(response);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                .body("Erro ao renovar o token: " + e.getMessage());
        }
    }

    @PostMapping("/logout")
    public ResponseEntity<?> logout(HttpServletRequest request,
                                    HttpServletResponse response) {
        
        cookieUtils.clearTokens(response);
        
        return ResponseEntity.ok().body(Map.of(
            "message", "Logout realizado com sucesso"
        ));
    }

    @GetMapping("/me")
    public ResponseEntity<?> getCurrentUser(@CookieValue(
                    name="accessToken", required = false) String token){
        
        if(!jwtUtils.isAccessToken(token)){
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
        }

        String userName = jwtUtils.getUsernameDoToken(token);
        ShopUserDetails user = (ShopUserDetails) userDetailsService.loadUserByUsername(userName);
        

        List<String> roles = user.getAuthorities().stream()  
                                .map(role -> role.toString())
                                .collect(Collectors.toList());

        Map<String, Object> responseBody = new HashMap<>();                                
        responseBody.put("email", user.getEmail());
        responseBody.put("roles", roles);
        responseBody.put("id", user.getId());
        responseBody.put("nome", user.getNome());
        responseBody.put("sobrenome", user.getSobrenome());
        responseBody.put("telefone", user.getTelefone());

        return ResponseEntity.ok(responseBody);
    }
}
