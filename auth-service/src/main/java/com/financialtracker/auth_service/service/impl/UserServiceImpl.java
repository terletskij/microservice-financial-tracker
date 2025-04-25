package com.financialtracker.auth_service.service.impl;

import com.financialtracker.auth_service.dto.JwtAuthenticationDto;
import com.financialtracker.auth_service.dto.RefreshTokenDto;
import com.financialtracker.auth_service.dto.UserDto;
import com.financialtracker.auth_service.dto.request.LoginRequest;
import com.financialtracker.auth_service.dto.request.RegisterRequest;
import com.financialtracker.auth_service.entity.User;
import com.financialtracker.auth_service.enums.Role;
import com.financialtracker.auth_service.exceptions.AlreadyExistsException;
import com.financialtracker.auth_service.exceptions.InvalidTokenException;
import com.financialtracker.auth_service.exceptions.UserNotFoundException;
import com.financialtracker.auth_service.repository.UserRepository;
import com.financialtracker.auth_service.security.jwt.JwtService;
import com.financialtracker.auth_service.service.UserService;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
@RequiredArgsConstructor
public class UserServiceImpl implements UserService {

    private final JwtService jwtService;
    private final UserRepository userRepository;
    private final ModelMapper modelMapper;
    private final PasswordEncoder passwordEncoder;

    @Override
    public JwtAuthenticationDto logIn(LoginRequest loginRequest) {
        User user = findByCredentials(loginRequest);
        return jwtService.generateAuthToken(user.getEmail(), user.getId());
    }

    @Override
    public JwtAuthenticationDto refreshToken(RefreshTokenDto refreshTokenDto) {
        String refreshToken = refreshTokenDto.getRefreshToken();
        if (refreshToken != null && jwtService.validateJwt(refreshToken)) {
            User user = findByEmail(jwtService.getEmailFromToken(refreshToken));
            return jwtService.refreshBaseToken(user.getEmail(), user.getId(), refreshToken);
        }
        throw new InvalidTokenException("Invalid refresh token");
    }

    @Override
    public User createUser(RegisterRequest request) {
        if (userRepository.existsByEmail(request.getEmail())) {
            throw new AlreadyExistsException("User with this email already exists");
        }
        User user = modelMapper.map(request, User.class);
        user.setPassword(passwordEncoder.encode(user.getPassword()));
        user.setRole(Role.USER);
        return userRepository.save(user);
    }

    @Override
    public User getUserById(Long id) {
        return userRepository.findById(id).orElseThrow(() -> new UserNotFoundException("User not found"));
    }

    @Override
    public User findByCredentials(LoginRequest request) {
        Optional<User> optionalUser = userRepository.findByEmail(request.getEmail());
        if (optionalUser.isPresent()) {
            User user = optionalUser.get();
            if (passwordEncoder.matches(request.getPassword(), user.getPassword())) {
                return user;
            }
        }
        throw new BadCredentialsException("Email or password are incorrect");
    }

    @Override
    public User findByEmail(String email) {
        return userRepository.findByEmail(email).orElseThrow(() -> new EntityNotFoundException("User not found"));
    }

    @Override
    public UserDto convertToDto(User user) {
        return modelMapper.map(user, UserDto.class);
    }

}