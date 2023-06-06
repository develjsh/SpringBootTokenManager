package com.example.springboottokenmanager.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.example.springboottokenmanager.entity.Token;

public interface TokenRepository extends JpaRepository<Token, Long>{
}
