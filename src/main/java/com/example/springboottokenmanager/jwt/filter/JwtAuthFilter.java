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
import org.springframework.util.StringUtils;
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
        String accessToken = jwtUtil.getHeaderToken(request, "Access");
        String refreshToken = jwtUtil.getHeaderToken(request, "Refresh");
        boolean b = StringUtils.hasText(accessToken);
        if(StringUtils.hasText(accessToken)) {
            if(jwtUtil.tokenValidation(accessToken)) {
                setAuthentication(accessToken);
            };
        } else if (StringUtils.hasText(refreshToken)) {
            boolean isRefreshToken = jwtUtil.refreshTokenValidation(refreshToken);
            if (isRefreshToken) {
                String userId = jwtUtil.getInfoFromToken(refreshToken, "userId");
                String username = jwtUtil.getInfoFromToken(refreshToken, "username");
                
                String newAccessToken = jwtUtil.createToken(userId, username, "Access");

                jwtUtil.setHeaderAccessToken(response, newAccessToken);
                setAuthentication(newAccessToken);
            } else {
                jwtExceptionHandler(response, "RefreshToken Expired", HttpStatus.BAD_REQUEST);
                return;
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
    public void setAuthentication(String token) {
        Authentication authentication = jwtUtil.getAuthentication(token);
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