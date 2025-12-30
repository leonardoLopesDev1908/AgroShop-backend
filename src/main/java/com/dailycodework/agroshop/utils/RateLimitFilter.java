package com.dailycodework.agroshop.utils;

import java.io.IOException;
import java.time.Duration;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;


import io.github.bucket4j.Bandwidth;
import io.github.bucket4j.Bucket;
import io.github.bucket4j.Bucket4j;
import jakarta.servlet.Filter;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.ServletRequest;
import jakarta.servlet.ServletResponse;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

public class RateLimitFilter implements Filter {

    private final Map<String, Bucket> ipBucketMap = new ConcurrentHashMap<>();

    private static final Set<String> EXCLUDED_PATHS = Set.of(
        "/api/v1/produto/imagens",
        "/api/v1/produtos"
    );

    private Bucket createNewBucket() {
        return Bucket4j.builder()
                .addLimit(Bandwidth.simple(50, Duration.ofMinutes(1)))
                .build();
    }
    
    @Override
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain)
            throws IOException, ServletException {

        HttpServletRequest req = (HttpServletRequest) request;
        String ip = req.getHeader("X-Forwarded-For");
        String path = req.getRequestURI();

        if(ip != null){
            ip = ip.split(",")[0].trim();
        } else {
            ip = req.getRemoteAddr();
        }

        if(isExcluded(path)){
            chain.doFilter(request, response);
            return;
        }

        Bucket bucket = ipBucketMap.computeIfAbsent(ip, k -> createNewBucket());
        if (bucket.tryConsume(1)) {
            chain.doFilter(request, response);
            return;
        } 
            
        HttpServletResponse httpResp = (HttpServletResponse) response;
        httpResp.setStatus(429);
        httpResp.getWriter().write("Rate limit exceeded. Try again later.");
    }

    private boolean isExcluded(String path){
        return EXCLUDED_PATHS.stream().anyMatch(path::startsWith);
    }
}