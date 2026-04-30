package co.empresa.proyecto_desarrollo3.auth.dto;

import lombok.Data;

@Data
public class LoginRequest {
    private String username;
    private String password;

    
}