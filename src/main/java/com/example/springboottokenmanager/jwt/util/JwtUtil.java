package com.example.springboottokenmanager.jwt.util;

import java.util.Base64;
import java.util.Date;

import javax.annotation.PostConstruct;
import javax.crypto.SecretKey;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Component;

import com.example.springboottokenmanager.dto.AccountDto;
import com.example.springboottokenmanager.jwt.dto.TokenDto;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.MalformedJwtException;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.UnsupportedJwtException;
import io.jsonwebtoken.security.Keys;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Component
@RequiredArgsConstructor
public class JwtUtil {
    private static final long ACCESS_TIME =  30 * 60 * 1000L;
    private static final long REFRESH_TIME =  7 * 24 * 60 * 60 * 1000L;
    public static final String ACCESS_TOKEN = "Access_Token";
    public static final String REFRESH_TOKEN = "Refresh_Token";

    @Value("${jwt.secret}")
    private String secretKey;
    private SecretKey key;
    private final SignatureAlgorithm signatureAlgorithm = SignatureAlgorithm.HS256;

    @PostConstruct
    public void init() {
        byte[] bytes = Base64.getDecoder().decode(secretKey);
        this.key = Keys.hmacShaKeyFor(bytes);
    }
    
    public String getHeaderToken(HttpServletRequest request, String type) {
        return type.equals("Access") ? request.getHeader(ACCESS_TOKEN) :request.getHeader(REFRESH_TOKEN);
    }

    public TokenDto createAllToken(String userId, String username) {
        return new TokenDto(createToken(userId, username, "Access"), createToken(userId, username, "Refresh"));
    }
    
    /**
     * token 생성
     * @param userId
     * @param type
     * @return
     */
    public String createToken(String userId, String username, String type) {
        Date date = new Date();
        long time = type.equals("Access") ? ACCESS_TIME : REFRESH_TIME;
        Claims claims = Jwts.claims();
        claims.put("userId", userId);
        claims.put("username", username);

        return Jwts.builder()
                .setClaims(claims)
                .setSubject(userId)
                .setExpiration(new Date(date.getTime() + time))
                .setIssuedAt(date)
                .signWith(key, signatureAlgorithm)
                .compact();
    }

    /**
     * token 검증
     * @param token
     * @return
     */
    public Boolean tokenValidation(String token) {
        try {
            Jwts.parserBuilder().setSigningKey(key).build().parseClaimsJws(token);
            return true;
        } catch (MalformedJwtException e) {
            log.error("Invalid JWT token", e);
          } catch (ExpiredJwtException e) {
            log.error("Expired JWT token", e);
          } catch (UnsupportedJwtException e) {
            log.error("Unsupported JWT token", e);
          } catch (IllegalArgumentException e) {
            log.error("JWT claims string is empty.", e);
          }
          return false;
    }

    /**
     * refreshToken 검증
     * @param token
     * @return
     */
    public Boolean refreshTokenValidation(String token) {
        if(!tokenValidation(token)) return false;
        return true;
    }
    
    /**
     * Token을 통해 userId 가져오는 기능
     * @param token
     * @return
     */
    public String getUserIdFromToken(String token) {
        return Jwts.parserBuilder().setSigningKey(key).build().parseClaimsJws(token).getBody().getSubject();
    }

    /**
     * Token을 통해 저장되어 있는 값 가져오는 기능
     * getInfoToken
     * @param token
     * @return
     */
    public String getInfoFromToken(String token, String claimsKey) {
        return (String)Jwts.parserBuilder().setSigningKey(key).build().parseClaimsJws(token).getBody().get(claimsKey);
    }

    /**
     * 인증 객체 생성
     * @param toekn
     * @return
     */
    public Authentication getAuthentication(String toekn) {
        String userId = getInfoFromToken(toekn, "userId");
        String username = getInfoFromToken(toekn, "username");
        
        AccountDto accountDto = new AccountDto();
        accountDto.setUserId(userId);
        accountDto.setUsername(username);
        return new UsernamePasswordAuthenticationToken(accountDto, "", null);
    }

    /**
     * header에 accessToken 설정
     * @param response
     * @param accessToken
     */
    public void setHeaderAccessToken(HttpServletResponse response, String accessToken) {
        response.setHeader("Access_Token", accessToken);
    }

    /**
     * header에 refreshToken 설정
     * @param response
     * @param refreshToken
     */
    public void setHeaderRefreshToken(HttpServletResponse response, String refreshToken) {
        response.setHeader("Refresh_Token", refreshToken);
    }
}