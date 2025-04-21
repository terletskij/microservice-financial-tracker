package com.financialtracker.auth_service.service;

import com.financialtracker.auth_service.dto.JwtAuthenticationDto;
import com.financialtracker.auth_service.dto.RefreshTokenDto;
import com.financialtracker.auth_service.dto.request.LoginRequest;
import com.financialtracker.auth_service.dto.request.RegisterRequest;
import com.financialtracker.auth_service.entity.User;
import org.apache.tomcat.websocket.AuthenticationException;

import java.util.List;

public interface UserService {

    JwtAuthenticationDto logIn(LoginRequest loginRequest) throws AuthenticationException;

    JwtAuthenticationDto refreshToken(RefreshTokenDto refreshTokenDto) throws AuthenticationException;

    User createUser(RegisterRequest request);

    User getUserById(Long id);
}
