package com.example.springboottokenmanager.service;

import javax.servlet.http.HttpServletRequest;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import com.example.springboottokenmanager.dto.AccountReqDto;
import com.example.springboottokenmanager.dto.AccountResDto;
import com.example.springboottokenmanager.dto.ResponseDto;
import com.example.springboottokenmanager.jwt.dto.TokenDto;
import com.example.springboottokenmanager.jwt.util.JwtUtil;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class TokenService {
    private Logger logger = LoggerFactory.getLogger(TokenService.class);
    private final JwtUtil jwtUtil;
    
    public ResponseDto createToken(AccountReqDto accountReqDto) {
        TokenDto tokenDto = jwtUtil.createAllToken(accountReqDto.getUserId(), accountReqDto.getUsername());
        return new ResponseDto("Success", HttpStatus.OK.value(), tokenDto);
    }

    public AccountResDto getUserInfo(HttpServletRequest request) {
        String userId = null, username = null;

        try {
            String accessToken = jwtUtil.getHeaderToken(request, "Access");

            if(accessToken.isBlank()) {
                accessToken = jwtUtil.getHeaderToken(request, "Refesh");
            };

            userId = jwtUtil.getInfoFromToken(accessToken, "userId");
            username = jwtUtil.getInfoFromToken(accessToken, "username");
        } catch (IllegalArgumentException e){
            logger.error("Expired JWT token", e);
        }
        
        return new AccountResDto(userId, username);
    }
}
