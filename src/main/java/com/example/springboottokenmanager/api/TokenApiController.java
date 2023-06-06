package com.example.springboottokenmanager.api;

import org.springframework.web.bind.annotation.RestController;

import com.example.springboottokenmanager.dto.AccountReqDto;
import com.example.springboottokenmanager.dto.ResponseDto;
import com.example.springboottokenmanager.jwt.dto.TokenDto;
import com.example.springboottokenmanager.jwt.util.JwtUtil;
import com.example.springboottokenmanager.service.TokenService;

import lombok.RequiredArgsConstructor;

import javax.servlet.http.HttpServletResponse;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;


@RestController
@RequiredArgsConstructor
public class TokenApiController {
    private final JwtUtil jwtUtil;
    private final TokenService tokenService;

    @PostMapping(value="/api/createToken")
    public ResponseEntity<ResponseDto> postMethodName(HttpServletResponse response, @RequestBody AccountReqDto AccountReqDto) {
        ResponseDto responseDto = tokenService.createToken(AccountReqDto);
        setHeader(response, responseDto.getToken());

        return new ResponseEntity<>(responseDto, HttpStatus.OK);
    }

    private void setHeader(HttpServletResponse response, TokenDto tokenDto) {
        response.addHeader(JwtUtil.ACCESS_TOKEN, tokenDto.getAccessToken());
        response.addHeader(JwtUtil.REFRESH_TOKEN, tokenDto.getRefreshToken());
    }
}