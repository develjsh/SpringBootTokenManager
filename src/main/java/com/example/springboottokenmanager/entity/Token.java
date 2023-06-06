package com.example.springboottokenmanager.entity;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.validation.constraints.NotBlank;

import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Getter
@NoArgsConstructor
public class Token {
    @Id @GeneratedValue
    private Long id;
    
    @NotBlank
    private String refreshToken;

    @NotBlank
    private String userId;
    
    @NotBlank
    private String username;

    public Token(String token, String userId, String username) {
        this.refreshToken = token;
    }

    public Token updateToken(String token) {
        this.refreshToken = token;
        return this;
    }
}
