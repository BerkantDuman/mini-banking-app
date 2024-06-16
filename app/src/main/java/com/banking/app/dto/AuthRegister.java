package com.banking.app.dto;

import jakarta.validation.constraints.NotBlank;
import lombok.*;

@Setter
@Getter
public class AuthRegister  extends AuthRequest {

    @NotBlank(message = "Email must not be blank")
    private String email;

    public AuthRegister(String username, String password, String email) {
        super(username, password);
        this.email = email;
    }
}
