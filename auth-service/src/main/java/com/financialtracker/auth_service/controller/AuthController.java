package com.financialtracker.auth_service.controller;

import com.financialtracker.auth_service.dto.response.JwtAuthenticationResponse;
import com.financialtracker.auth_service.dto.request.RefreshTokenRequest;
import com.financialtracker.auth_service.dto.UserDto;
import com.financialtracker.auth_service.dto.request.LoginRequest;
import com.financialtracker.auth_service.dto.request.RegisterRequest;
import com.financialtracker.auth_service.dto.response.ApiResponse;
import com.financialtracker.auth_service.entity.User;
import com.financialtracker.auth_service.exceptions.AlreadyExistsException;
import com.financialtracker.auth_service.exceptions.InvalidTokenException;
import com.financialtracker.auth_service.service.UserService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RequiredArgsConstructor
@RestController
@RequestMapping("/auth")
public class AuthController {

    private final UserService userService;

    @PostMapping("/log-in")
    public ResponseEntity<ApiResponse> logIn(@Valid @RequestBody LoginRequest request) {
        try {
            JwtAuthenticationResponse authDto = userService.logIn(request);
            return ResponseEntity.ok(new ApiResponse("Login successful", authDto));
        } catch (BadCredentialsException e) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
                    .body(new ApiResponse("Invalid credentials", null));
        }
    }

    @PostMapping("/refresh")
    public ResponseEntity<ApiResponse> refresh(@RequestBody RefreshTokenRequest refreshTokenRequest) {
        try {
            JwtAuthenticationResponse refreshedToken = userService.refreshToken(refreshTokenRequest);
            return ResponseEntity.ok(new ApiResponse("Success", refreshedToken));
        } catch (InvalidTokenException e) {
            return ResponseEntity.badRequest().body(new ApiResponse(e.getMessage(), null));
        }
    }

    @PostMapping("/register")
    public ResponseEntity<ApiResponse> register(@Valid @RequestBody RegisterRequest request) {
        try {
            User user = userService.createUser(request);
            UserDto userDto = userService.convertToDto(user);
            return ResponseEntity.status(HttpStatus.CREATED)
                    .body(new ApiResponse("User successfully registered", userDto));
        } catch (AlreadyExistsException e) {
            return ResponseEntity.badRequest()
                    .body(new ApiResponse(e.getMessage(), null));
        }
    }
}