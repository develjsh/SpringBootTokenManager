package com.example.springboottokenmanager.service;

import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import com.example.springboottokenmanager.dto.AccountReqDto;
import com.example.springboottokenmanager.dto.ResponseDto;
import com.example.springboottokenmanager.jwt.dto.TokenDto;
import com.example.springboottokenmanager.jwt.util.JwtUtil;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class TokenService {
    private final JwtUtil jwtUtil;
    
    public ResponseDto createToken(AccountReqDto accountReqDto) {
        TokenDto tokenDto = jwtUtil.createAllToken(accountReqDto.getUserId());
        return new ResponseDto("Success", HttpStatus.OK.value(), tokenDto);
    }
}
