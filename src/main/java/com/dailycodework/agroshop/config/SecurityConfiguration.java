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
   
    private static final List<String> SECURED_URLS =
            List.of(API + "/carrinhos/**", API+"/itemCarrinho/**", API+"/pedidos/**");

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
            .authorizeHttpRequests(authorize -> {
                authorize.requestMatchers(SECURED_URLS.toArray(String[]::new)).authenticated();
                authorize.requestMatchers("/css/**", "/js/**", "/images/**", "/webjars/**").permitAll();
                authorize.requestMatchers("/api/v1/login/**").permitAll();
                authorize.requestMatchers("/api/v1/auth/**").permitAll();
                authorize.requestMatchers("/api/v1/produtos/distintos/produtos").permitAll();
                authorize.requestMatchers("/api/v1/produtos/produtos").permitAll();
                authorize.requestMatchers("/api/v1/produtos/cadastrar").permitAll();
                authorize.requestMatchers("/api/v1/imagens/imagem/download/**").permitAll();
                authorize.requestMatchers("/api/v1/imagens/upload").permitAll();
                authorize.requestMatchers("/api/v1/usuarios/usuario/{email}").permitAll();
                authorize.requestMatchers("/api/v1/usuarios/cadastrar").permitAll();
                authorize.requestMatchers("/api/v1/produtos/produto/{id}/produto").permitAll();
                authorize.requestMatchers("/pedido/pesquisar").hasAuthority("GERENTE");
                authorize.anyRequest().authenticated();
            })
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
