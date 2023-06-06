package com.example.springboottokenmanager.dto;

import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
public class AccountReqDto {
    private String userId;
    private String username;

    public AccountReqDto(String userId, String username) {
        this.userId = userId;
        this.username = username;
    }
}
