package com.dailycodework.agroshop.security.jwt;

import java.io.IOException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;
import org.springframework.web.filter.OncePerRequestFilter;

import com.dailycodework.agroshop.security.user.ShopUserDetailsService;
import com.dailycodework.agroshop.utils.CookieUtils;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

@Component
public class AuthTokenFilter extends OncePerRequestFilter{

    @Autowired
    private JwtUtils jwtUtils;
    
    @Autowired 
    private CookieUtils cookieUtils;

    @Autowired
    private ShopUserDetailsService userDetailsService;

    @Override
    protected void doFilterInternal(
            HttpServletRequest request,
            HttpServletResponse response,
            FilterChain filterChain
    ) throws ServletException, IOException {

        if("OPTIONS".equalsIgnoreCase(request.getMethod())){
            filterChain.doFilter(request, response);
            return;
        }

        try {
            String jwt = cookieUtils.getAccessTokenFromCookies(request);

            if (StringUtils.hasText(jwt) && jwtUtils.validateToken(jwt)) {
                String username = jwtUtils.getUsernameDoToken(jwt);
                UserDetails userDetails = userDetailsService.loadUserByUsername(username);

                var auth = new UsernamePasswordAuthenticationToken(
                    userDetails, null, userDetails.getAuthorities()
                );
                SecurityContextHolder.getContext().setAuthentication(auth);
            }

        } catch (Exception e) {
            SecurityContextHolder.clearContext();
        }

        filterChain.doFilter(request, response);
    }

    // private void sendErroResponse(HttpServletResponse response) throws IOException{
    //     System.out.println("ERROR RESPONSE USERDETAILS");
    //     response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
    //     response.setContentType("application/json");
    //     ErroResponse erro = new ErroResponse("Acesso inválido");
    //     ObjectMapper mapper = new ObjectMapper();
    //     String jsonResponse = mapper.writeValueAsString(erro);
    //     response.getWriter().write(jsonResponse);
    // }

    
}
