package com.banking.app.service;

import com.banking.app.dto.AuthRegister;
import com.banking.app.dto.AuthRequest;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;

public interface AuthService {
    UsernamePasswordAuthenticationToken authenticate(AuthRequest authRequest);

    String encodePassword(AuthRegister authRegister);
}
