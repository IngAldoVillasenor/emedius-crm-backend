package com.emelius.crmbackend.controller;

import com.emelius.crmbackend.dto.request.RegisterRequestDTO;
import com.emelius.crmbackend.service.AuthService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import com.emelius.crmbackend.dto.request.LoginRequestDTO;
import com.emelius.crmbackend.dto.response.AuthResponseDTO;

@RestController
@RequestMapping("/api/auth")
public class AuthController {

    private final AuthService authService;

    public AuthController(AuthService authService) {
        this.authService = authService;
    }

    @PostMapping("/register")
    public ResponseEntity<String> register(@Valid @RequestBody RegisterRequestDTO request) {
        String responseMessage = authService.registerUser(request);
        return new ResponseEntity<>(responseMessage, HttpStatus.CREATED);
    }

    @PostMapping("/login")
    public ResponseEntity<AuthResponseDTO> login(@Valid @RequestBody LoginRequestDTO request) {
        String token = authService.login(request);
        return ResponseEntity.ok(new AuthResponseDTO(token));
    }
}