package com.banking.app.service;

import com.banking.app.auth.JwtService;
import com.banking.app.dto.AuthRegister;
import com.banking.app.dto.AuthRequest;
import com.banking.app.dto.AuthResponse;
import com.banking.app.model.User;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UserDetails;

public interface UserService {
    User findUserByUsername(String username);
    UserDetails loadUserByUsername(String username);
    User createUser(AuthRegister authRegister);
    AuthResponse login(AuthRequest authRequest, AuthenticationManager authenticationManager, JwtService jwtService);
    User getAuthenticatedUser(Authentication authentication);
}
