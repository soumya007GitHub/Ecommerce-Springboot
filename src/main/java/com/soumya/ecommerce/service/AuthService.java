package com.soumya.ecommerce.service;

import com.soumya.ecommerce.dto.AuthResponse;
import com.soumya.ecommerce.dto.LoginRequest;
import com.soumya.ecommerce.dto.RegisterRequest;

public interface AuthService {

    AuthResponse register(RegisterRequest request);

    AuthResponse login(LoginRequest request);
}
