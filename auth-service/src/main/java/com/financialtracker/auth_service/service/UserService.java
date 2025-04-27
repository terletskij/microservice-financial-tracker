package com.financialtracker.auth_service.service;

import com.financialtracker.auth_service.dto.response.JwtAuthenticationResponse;
import com.financialtracker.auth_service.dto.request.RefreshTokenRequest;
import com.financialtracker.auth_service.dto.UserDto;
import com.financialtracker.auth_service.dto.request.LoginRequest;
import com.financialtracker.auth_service.dto.request.RegisterRequest;
import com.financialtracker.auth_service.entity.User;

public interface UserService {

    JwtAuthenticationResponse logIn(LoginRequest loginRequest);

    JwtAuthenticationResponse refreshToken(RefreshTokenRequest refreshTokenRequest);

    User createUser(RegisterRequest request);

    User getUserById(Long id);

    User findByCredentials(LoginRequest request);

    User findByEmail(String email);

    UserDto convertToDto(User user);

}
