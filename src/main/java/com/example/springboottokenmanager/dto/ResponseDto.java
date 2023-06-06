package com.example.springboottokenmanager.dto;

import com.example.springboottokenmanager.jwt.dto.TokenDto;

import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
public class ResponseDto {
    private String msg;
    private int statusCode;
    private TokenDto token;

    public ResponseDto(String msg, int statusCode) {
        this.msg = msg;
        this.statusCode = statusCode;
    }

    public ResponseDto(String msg, int statusCode, TokenDto token) {
        this.msg = msg;
        this.statusCode = statusCode;
        this.token = token;
    }
}
