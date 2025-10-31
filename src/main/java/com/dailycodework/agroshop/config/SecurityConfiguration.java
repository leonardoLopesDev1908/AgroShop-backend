package com.dailycodework.agroshop.config;

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

import jakarta.annotation.PostConstruct;
import lombok.RequiredArgsConstructor;

@Configuration
@EnableWebSecurity
@RequiredArgsConstructor
@EnableMethodSecurity(securedEnabled=true, jsr250Enabled=true)
public class SecurityConfiguration {
    
    private final ShopUserDetailsService userDetailsService;
    private final JwtEntryPoint authEntryPoint;
    
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
            API + "/auth/**",
            API + "/produtos/distintos/produtos",
            API + "/produtos/produtos",
            API + "/imagens/imagem/download/**",
            API + "/usuarios/cadastrar",
            API + "/produtos/produto/*/produto"
        };
        
        FUNCIONARIO_ENDPOINTS = new String[] {
            API + "/produtos/cadastrar",
            API + "/imagens/upload",
            API + "/pedidos/usuario/pedidos",
            API + "/pedidos/pedido/*/excluir",
            API + "/pedidos/pedido/*/atualizar"
        };

        GERENTE_ENDPOINTS = new String[] {
        };

        AUTHENTICATED_ENDPOINTS = new String[] {
            API + "/carrinho/**",
            API + "/itens/**",
            API + "/pedidos/**"
        };
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
    
        return http
            .csrf(AbstractHttpConfigurer::disable)
            .cors(Customizer.withDefaults())
            .exceptionHandling(exception -> exception.authenticationEntryPoint(authEntryPoint))
            .sessionManagement(session -> session.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
            .authenticationProvider(authProvider())
            .addFilterBefore(authTokenFilter(), UsernamePasswordAuthenticationFilter.class)
            .httpBasic(Customizer.withDefaults())
            .authorizeHttpRequests(auth -> auth
                .requestMatchers(org.springframework.http.HttpMethod.OPTIONS, "/**").permitAll()
                .requestMatchers(PUBLIC_ENDPOINTS).permitAll()
                .requestMatchers(AUTHENTICATED_ENDPOINTS).authenticated()
                .requestMatchers(FUNCIONARIO_ENDPOINTS).hasAnyAuthority("Gerente", "Funcionario", "ADM")
                .requestMatchers(GERENTE_ENDPOINTS).hasAuthority("Gerente")
                .anyRequest().permitAll()
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
