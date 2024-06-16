package com.banking.app.service;

import com.banking.app.auth.JwtService;
import com.banking.app.dto.AuthRegister;
import com.banking.app.dto.AuthRequest;
import com.banking.app.dto.AuthResponse;
import com.banking.app.model.User;
import com.banking.app.model.UserDetail;
import com.banking.app.repository.UserRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.*;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.Optional;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.is;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class UserServiceImplTest {

    @InjectMocks
    private UserServiceImpl userService;
    @Mock
    private UserRepository userRepository;
    @Mock
    private PasswordEncoder passwordEncoder;
    @Mock
    private AuthenticationManager authenticationManager;
    @Mock
    private JwtService jwtService;
    @Mock
    private Authentication authentication;

    private User user;
    private String username = "user";
    private AuthRequest authRequest = new AuthRequest(username, "password");
    private AuthRegister authRegister = new AuthRegister(username, "password", "email");

    @BeforeEach
    void setUp() {
        user = new User();
        user.setUsername(username);
    }

    @Test
    void shouldReturnUser_whenUsernameIsValid() {
        when(userRepository.findByUsername(anyString())).thenReturn(java.util.Optional.of(user));

        User result = userService.findUserByUsername(username);

        assertThat(result.getUsername(), is(user.getUsername()));

        verify(userRepository, times(1)).findByUsername(anyString());
    }

    @Test
    void shouldThrowException_whenUsernameIsNotValid() {
        when(userRepository.findByUsername(anyString())).thenReturn(java.util.Optional.empty());

        assertThrows(UsernameNotFoundException.class, () -> userService.findUserByUsername("invalidUsername"));
    }

    @Test
    void shouldCreateUserSuccessfully() {
        when(passwordEncoder.encode(authRegister.getPassword())).thenReturn("encodedPassword");
        when(userRepository.save(any(User.class))).thenReturn(user);

        User result = userService.createUser(authRegister);

        assertThat(result.getUsername(), is(user.getUsername()));
        verify(userRepository).save(any(User.class));
    }

    @Test
    void shouldLoadUserByUsernameSuccessfully() {
        when(userRepository.findByUsername(anyString())).thenReturn(Optional.of(user));

        UserDetail result = (UserDetail) userService.loadUserByUsername(username);

        assertThat(result.getUsername(), is(user.getUsername()));
        verify(userRepository).findByUsername(anyString());
    }

    @Test
    void shouldReturnAuthenticatedUserSuccessfully() {
        when(authentication.getName()).thenReturn(username);
        when(userRepository.findByUsername(anyString())).thenReturn(Optional.of(user));

        User result = userService.getAuthenticatedUser(authentication);

        assertThat(result.getUsername(), is(user.getUsername()));
        verify(userRepository).findByUsername(anyString());
    }

    @Test
    void shouldLoginSuccessfully() {
        when(userRepository.findByUsername(anyString())).thenReturn(Optional.of(user));
        when(authenticationManager.authenticate(any())).thenReturn(authentication);
        when(authentication.isAuthenticated()).thenReturn(true);
        when(jwtService.generateToken(anyString())).thenReturn("testToken");

        AuthResponse result = userService.login(authRequest, authenticationManager, jwtService);

        assertThat(result.getAccessToken(), is("testToken"));
        verify(authenticationManager).authenticate(any());
    }
}