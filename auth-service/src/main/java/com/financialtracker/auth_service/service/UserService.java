package com.financialtracker.auth_service.service;

import com.financialtracker.auth_service.dto.request.RegisterRequest;
import com.financialtracker.auth_service.entity.User;

import java.util.List;

public interface UserService {

    User createUser(RegisterRequest request);

    User getUserById(Long id);
}
