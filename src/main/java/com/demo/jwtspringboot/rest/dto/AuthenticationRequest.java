package com.demo.jwtspringboot.rest.dto;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class AuthenticationRequest {

    private String username;
    private String password;

    // Getter dan setter
}


