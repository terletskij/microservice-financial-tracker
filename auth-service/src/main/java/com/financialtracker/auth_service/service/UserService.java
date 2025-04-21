package com.financialtracker.auth_service.service;

import com.financialtracker.auth_service.dto.JwtAuthenticationDto;
import com.financialtracker.auth_service.dto.RefreshTokenDto;
import com.financialtracker.auth_service.dto.UserDto;
import com.financialtracker.auth_service.dto.request.LoginRequest;
import com.financialtracker.auth_service.dto.request.RegisterRequest;
import com.financialtracker.auth_service.entity.User;
import org.apache.tomcat.websocket.AuthenticationException;

import java.util.List;

public interface UserService {

    JwtAuthenticationDto logIn(LoginRequest loginRequest);

    JwtAuthenticationDto refreshToken(RefreshTokenDto refreshTokenDto);

    User createUser(RegisterRequest request);

    User getUserById(Long id);

    User findByCredentials(LoginRequest request);

    User findByEmail(String email);

    UserDto convertToDto(User user);

}
