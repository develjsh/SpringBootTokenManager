package com.example.springboottokenmanager.dto;

import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
public class AccountResDto {
    private String userId;
    private String username;

    public AccountResDto(String userId, String username) {
        this.userId = userId;
        this.username = username;
    }
}
