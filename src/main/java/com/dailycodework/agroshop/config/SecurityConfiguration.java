package com.dailycodework.agroshop.config;

import java.util.List;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.core.GrantedAuthorityDefaults;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

import com.dailycodework.agroshop.security.jwt.AuthTokenFilter;
import com.dailycodework.agroshop.security.jwt.JwtEntryPoint;
import com.dailycodework.agroshop.security.user.ShopUserDetailsService;

import lombok.RequiredArgsConstructor;

@Configuration
@EnableWebSecurity
@RequiredArgsConstructor
@EnableMethodSecurity(securedEnabled=true, jsr250Enabled=true)
public class SecurityConfiguration {
    
    private final ShopUserDetailsService userDetailsService;
    private final JwtEntryPoint authEntryPoint;
    
    @Value("${api.prefix}")
    private static String API;
   
    private static final String[] PUBLIC_ENDPOINTS = {
        API + "/login/**",
        API + "/auth/**",
        API + "/produtos/distintos/produtos",
        API + "/produtos/produtos",
        API + "/produtos/produto/{id}/produto",
        API + "/imagens/imagem/download/**",
        API + "/imagens/upload",
        API + "/usuarios/usuario/{email}",
        API + "/usuarios/cadastrar",
        API + "/itens/item/cadastrar",
        API + "/carrinho/itens",
        "/css/**", "/js/**", "/images/**", "/webjars/**"
    };
    
    private static final String[] FUNCIONARIO_ENDPOINTS = {
        API + "/produtos/cadastrar",
        API + "/produtos/deletar/{id}",
        API + "/produtos/atualizar/{id}",
        API + "/pedido/pesquisar"
    };

    private static final String[] GERENTE_ENDPOINTS = {
        
    };

    private static final String[] AUTHENTICATED_ENDPOINTS = {
        API + "/carrinhos/**",
        API + "/itens/**",
        API + "/pedidos/**",
    };

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
    
        return http
            .csrf(AbstractHttpConfigurer::disable)
            .exceptionHandling(exception -> exception.authenticationEntryPoint(authEntryPoint))
            .sessionManagement(session -> session.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
            .authenticationProvider(authProvider())
            .addFilterBefore(authTokenFilter(), UsernamePasswordAuthenticationFilter.class)
            .httpBasic(Customizer.withDefaults())
            .authorizeHttpRequests(auth -> auth 
                .requestMatchers(PUBLIC_ENDPOINTS).permitAll()
                .requestMatchers(FUNCIONARIO_ENDPOINTS).hasAnyAuthority("Funcionario", "Gerente")
                .requestMatchers(AUTHENTICATED_ENDPOINTS).authenticated()
                // authorize.requestMatchers(GERENTE_ENDPOINTS)
                .anyRequest().authenticated()
            )
            .formLogin(form -> 
                form.loginPage("/login")
                .permitAll()
                .defaultSuccessUrl("/", true)    
            )
            .build();
    }
/* 
    @Bean
    public WebSecurityCustomizer webSecurityCustomizer(){
        return web -> web.ignoring().requestMatchers(
            "/v2/api-docs/**",
            "/v3/api-docs/**",
            "/swagger-resources/**",
            "/swagger-ui.html",
            "/swagger.ui/**",
            "/webjars/**"
        );
    }
    */
}
