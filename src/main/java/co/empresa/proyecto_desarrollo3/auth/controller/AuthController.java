package co.empresa.proyecto_desarrollo3.auth.controller;

import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import co.empresa.proyecto_desarrollo3.auth.dto.LoginRequest;
import co.empresa.proyecto_desarrollo3.auth.service.AuthService;

@RestController
@RequestMapping("/auth")
public class AuthController {

    @Autowired
    private AuthService authService;

    @PostMapping("/login")
    public ResponseEntity<String> login(@RequestBody LoginRequest request) {
        String response = authService.login(request.getUsername(), request.getPassword());
        return ResponseEntity.ok(response);
    }
    @PostMapping("/refresh")
    public ResponseEntity<String> refresh(@RequestBody Map<String, String> body) {
        String response = authService.refresh(body.get("refresh_token"));
        return ResponseEntity.ok(response);
    }
}