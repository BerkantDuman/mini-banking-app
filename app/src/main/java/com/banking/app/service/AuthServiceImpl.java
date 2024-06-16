package com.banking.app.service;

import com.banking.app.dto.AuthRegister;
import com.banking.app.dto.AuthRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
public class AuthServiceImpl implements AuthService {

    @Autowired
    private AuthenticationManager authenticationManager;

    @Autowired
    private PasswordEncoder passwordEncoder;


    public String encodePassword(AuthRegister authRegister){
        return passwordEncoder.encode(authRegister.getPassword());
    }

    public UsernamePasswordAuthenticationToken authenticate(AuthRequest authRequest){
       return new UsernamePasswordAuthenticationToken(authRequest.getUsername(), authRequest.getPassword());
    }
}
