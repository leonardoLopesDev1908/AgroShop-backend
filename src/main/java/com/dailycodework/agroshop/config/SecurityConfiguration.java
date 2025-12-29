package com.dailycodework.agroshop.config;

import java.util.List;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.core.GrantedAuthorityDefaults;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;

import com.dailycodework.agroshop.security.jwt.AuthTokenFilter;
import com.dailycodework.agroshop.security.jwt.JwtEntryPoint;
import com.dailycodework.agroshop.security.oauth2.CustomOAuth2UserService;
import com.dailycodework.agroshop.security.oauth2.OAuth2LoginSuccessHandler;
import com.dailycodework.agroshop.security.user.ShopUserDetailsService;

import jakarta.annotation.PostConstruct;
import lombok.RequiredArgsConstructor;

@Configuration
@EnableWebSecurity
@RequiredArgsConstructor
@EnableMethodSecurity(securedEnabled=true, jsr250Enabled=true)
public class SecurityConfiguration {
    
    private final ShopUserDetailsService userDetailsService;
    private final JwtEntryPoint authEntryPoint;
    private final OAuth2LoginSuccessHandler oAuth2LoginSuccessHandler;
    private final CustomOAuth2UserService customOAuth2UserService;

    @Value("${api.prefix}")
    private String API;

    private String[] PUBLIC_ENDPOINTS;
    private String[] FUNCIONARIO_ENDPOINTS;
    private String[] GERENTE_ENDPOINTS;
    private String[] AUTHENTICATED_ENDPOINTS;

    @PostConstruct
    public void initEndpoints() {
        PUBLIC_ENDPOINTS = new String[] {
            API + "/css/**", "/js/**", "/images/**", "/webjars/**",
            API + "/login/**",
            API + "/auth/login",
            API + "/oauth2/**",
            API + "/produtos/distintos/produtos",
            API + "/produtos/produtos",
            API + "/imagens/imagem/download/**",
            API + "/usuarios/cadastrar",
            API + "/produtos/produto/*/produto",
            API + "/melhorenvio/frete/produto/cotar",
            API + "/csrf"
        };
        
        FUNCIONARIO_ENDPOINTS = new String[] {
            API + "/produtos/cadastrar",
            API + "/imagens/upload",
            API + "/pedidos/usuario/pedidos",
            API + "/pedidos/pedido/*/excluir",
            API + "/pedidos/pedido/*/atualizar"
        };

        GERENTE_ENDPOINTS = new String[] {
            API + "/dashboard/**"
        };

        AUTHENTICATED_ENDPOINTS = new String[] {
            API + "/auth/me",
            API + "/carrinho/**",
            API + "/itens/**",
            API + "/pedidos/**",
            API + "/melhorenvio/frete/itens/cotar",
            API + "/produto/frete/**",
            API + "/itens/frete/**",
            API + "/payment/**"
        };
    }

    @Bean
    CorsConfigurationSource corsConfigurationSource() {
        CorsConfiguration config = new CorsConfiguration();

        config.setAllowedOrigins(List.of(
            "https://agro-shop-frontend-phi.vercel.app"
        ));

        config.setAllowedMethods(List.of(
            "GET", "POST", "PUT", "DELETE", "OPTIONS"
        ));

        config.setAllowedHeaders(List.of(
            "Content-Type",
            "Authorization",
            "X-Requested-With"
        ));

        config.setAllowCredentials(true);

        UrlBasedCorsConfigurationSource source =
            new UrlBasedCorsConfigurationSource();

        source.registerCorsConfiguration("/**", config);
        return source;
    }

    @Bean
    public AuthenticationManager authenticationManager(AuthenticationConfiguration authConfig) throws Exception{
        return authConfig.getAuthenticationManager();
    }

    @Bean
    public AuthTokenFilter authTokenFilter(){
        return new AuthTokenFilter();
    }

    @Bean
    public PasswordEncoder passwordEncoder(){
        return new BCryptPasswordEncoder();
    }

    @Bean
    public DaoAuthenticationProvider authProvider(){
        var authProvider = new DaoAuthenticationProvider();
        authProvider.setUserDetailsService(userDetailsService);
        authProvider.setPasswordEncoder(passwordEncoder());

        return authProvider;
    }
    @Bean
    public GrantedAuthorityDefaults grantedAuthorityDefaults(){
        return new GrantedAuthorityDefaults("");
    }

    @Bean
    public SecurityFilterChain defaultSecurityFilterChain(HttpSecurity http) throws Exception {
        http
            .cors(cors -> cors.configurationSource(corsConfigurationSource()))
            .cors(cors -> cors.disable())
            .exceptionHandling(exception -> exception.authenticationEntryPoint(authEntryPoint))
            .sessionManagement(session -> session.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
            .authenticationProvider(authProvider())
            .addFilterBefore(authTokenFilter(), UsernamePasswordAuthenticationFilter.class)
            .authorizeHttpRequests(auth -> auth
                .requestMatchers(HttpMethod.OPTIONS, "/**").permitAll()
                .requestMatchers(PUBLIC_ENDPOINTS).permitAll()
                .requestMatchers(AUTHENTICATED_ENDPOINTS).authenticated()
                .requestMatchers(FUNCIONARIO_ENDPOINTS).hasAnyAuthority("Gerente", "Funcionario", "ADM")
                .requestMatchers(GERENTE_ENDPOINTS).hasAuthority("Gerente")
                .anyRequest().permitAll()
            )
            .oauth2Login(oauth2 -> oauth2
                .userInfoEndpoint(userInfo -> userInfo.userService(customOAuth2UserService))
                .successHandler(oAuth2LoginSuccessHandler)
            );

        return http.build();
    }
}
