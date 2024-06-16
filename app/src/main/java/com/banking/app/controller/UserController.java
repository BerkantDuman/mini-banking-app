package com.banking.app.controller;

import com.banking.app.auth.JwtService;
import com.banking.app.dto.AuthRegister;
import com.banking.app.dto.AuthRequest;
import com.banking.app.service.UserService;
import jakarta.validation.Valid;
import lombok.AllArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/users")
@AllArgsConstructor
public class UserController {

    private final UserService userService;

    private JwtService jwtService;

    private AuthenticationManager authenticationManager;

    @PostMapping("/login")
    public ResponseEntity login(@RequestBody AuthRequest authRequest){
            return ResponseEntity.ok(userService.login(authRequest, authenticationManager, jwtService));
    }

    @PostMapping("/register")
    public ResponseEntity register(@Valid @RequestBody AuthRegister authRegister){
        return  ResponseEntity.ok(userService.createUser(authRegister));
    }
}
