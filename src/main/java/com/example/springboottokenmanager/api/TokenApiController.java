package com.example.springboottokenmanager.api;

import org.springframework.web.bind.annotation.RestController;

import com.example.springboottokenmanager.dto.AccountReqDto;
import com.example.springboottokenmanager.dto.AccountResDto;
import com.example.springboottokenmanager.dto.ResponseDto;
import com.example.springboottokenmanager.jwt.dto.TokenDto;
import com.example.springboottokenmanager.jwt.util.JwtUtil;
import com.example.springboottokenmanager.service.TokenService;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

@Slf4j
@RestController
@RequiredArgsConstructor
public class TokenApiController {
    private Logger lgoger = LoggerFactory.getLogger(TokenApiController.class);
    private final JwtUtil jwtUtil;
    private final TokenService tokenService;

    @PostMapping(value="/api/create/token")
    public ResponseEntity<ResponseDto> postCreateToken(HttpServletResponse response, @RequestBody AccountReqDto AccountReqDto) {
        ResponseDto responseDto = tokenService.createToken(AccountReqDto);
        setHeader(response, responseDto.getToken());

        return new ResponseEntity<>(responseDto, HttpStatus.OK);
    }

    @GetMapping(value="/api/search/userInfo")
    public ResponseEntity<AccountResDto> getUserInfo(HttpServletResponse response, HttpServletRequest request) {
        AccountResDto accountResDto = tokenService.getUserInfo(request);
        return new ResponseEntity<>(accountResDto, HttpStatus.OK);
    }

    private void setHeader(HttpServletResponse response, TokenDto tokenDto) {
        response.addHeader(JwtUtil.ACCESS_TOKEN, tokenDto.getAccessToken());
        response.addHeader(JwtUtil.REFRESH_TOKEN, tokenDto.getRefreshToken());
    }
}