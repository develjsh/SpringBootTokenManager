package com.example.springboottokenmanager.jwt.filter;

import java.io.IOException;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.filter.OncePerRequestFilter;

import com.example.springboottokenmanager.dto.ResponseDto;
import com.example.springboottokenmanager.jwt.util.JwtUtil;
import com.fasterxml.jackson.databind.ObjectMapper;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@RequiredArgsConstructor
public class JwtAuthFilter extends OncePerRequestFilter {
    private Logger logger = LoggerFactory.getLogger(JwtAuthFilter.class);
    private final JwtUtil jwtUtil;

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {
        // WebSecurityConfig 에서 보았던 UsernamePasswordAuthenticationFilter 보다 먼저 동작을 하게 됩니다.

        // Access / Refresh 헤더에서 토큰을 가져옴.
        String accessToken = jwtUtil.getHeaderToken(request, "Access");
        String refreshToken = jwtUtil.getHeaderToken(request, "Refresh");
        
        logger.info("accessToken = {}", accessToken);
        logger.info("refreshToken = {}", refreshToken);

        if(accessToken != null) {
            if(jwtUtil.tokenValidation(accessToken)) {
                setAuthentication(jwtUtil.getUserIdFromToken(accessToken));
            };
        } else if (refreshToken != null) {
            boolean isRefreshToken = jwtUtil.refreshTokenValidation(refreshToken);
            if (isRefreshToken) {
                String loginId = jwtUtil.getUserIdFromToken(refreshToken);
                String newAccessToken = jwtUtil.createToken(loginId, "Access");

                jwtUtil.setHeaderAccessToken(response, newAccessToken);
                setAuthentication(jwtUtil.getUserIdFromToken(newAccessToken));
            }; 
        } else {
            jwtExceptionHandler(response, "RefreshToken Expired", HttpStatus.BAD_REQUEST);
            return;
        };

        filterChain.doFilter(request,response);
    }

    /**
     * SecurityContext 에 Authentication 객체 저장
     * @param userId
     */
    public void setAuthentication(String userId) {
        Authentication authentication = jwtUtil.createAuthentication(userId);
        SecurityContextHolder.getContext().setAuthentication(authentication);
    }

    /**
     * Jwt 예외처리
     * @param response
     * @param msg
     * @param status
     */
    public void jwtExceptionHandler(HttpServletResponse response, String msg, HttpStatus status) {
        response.setStatus(status.value());
        response.setContentType("application/json");
        try {
            String json = new ObjectMapper().writeValueAsString(new ResponseDto(msg, status.value()));
            response.getWriter().write(json);
        } catch (Exception e) {
            log.error(e.getMessage());
        }
    }
}