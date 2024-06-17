package com.banking.app.service;

import com.banking.app.auth.JwtService;
import com.banking.app.dto.AuthRegister;
import com.banking.app.dto.AuthRequest;
import com.banking.app.dto.AuthResponse;
import com.banking.app.model.User;
import com.banking.app.model.UserDetail;
import com.banking.app.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationCredentialsNotFoundException;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import java.time.LocalDateTime;

@Service
public class UserServiceImpl implements UserDetailsService, UserService {

    @Autowired
    private  UserRepository userRepository;
    @Autowired
    private PasswordEncoder passwordEncoder;


    public User findUserByUsername(String username){
        return userRepository.findByUsername(username).orElseThrow(() ->
                new UsernameNotFoundException("Given user could not be found"));
    }

    @Override
    public AuthResponse login(AuthRequest authRequest, AuthenticationManager authenticationManager, JwtService jwtService) {

        User user = findUserByUsername(authRequest.getUsername());

        Authentication authentication = authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(user.getUsername(), authRequest.getPassword()));


        if (authentication.isAuthenticated()) {
            return AuthResponse.builder()
                    .accessToken(jwtService.generateToken(authRequest.getUsername()))
                    .build();

        }else {
            throw new AuthenticationCredentialsNotFoundException("Invalid username or password");
        }
    }

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        User user =  findUserByUsername(username);
        return new UserDetail(user);
    }

    @Override
    public User createUser(AuthRegister authRegister) {
            User user = User.builder()
                    .createdAt(LocalDateTime.now())
                    .email(authRegister.getEmail())
                    .username(authRegister.getUsername())
                    .password(passwordEncoder.encode(authRegister.getPassword()))
                    .build();

            return userRepository.save(user);
    }

    @Override
    public User getAuthenticatedUser(Authentication authentication) {
        String username = authentication.getName();
        return findUserByUsername(username);
    }
}
