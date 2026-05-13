package com.ecommerce.controller;

import com.ecommerce.config.JwtUtil;
import com.ecommerce.dto.ClienteProfileResponse;
import com.ecommerce.dto.UpdateProfileRequest;
import com.ecommerce.model.Cliente;
import com.ecommerce.service.AuthService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/user")
@CrossOrigin(origins = "*", allowedHeaders = "*")
public class UserController {

    private final AuthService authService;
    private final JwtUtil jwtUtil;

    public UserController(AuthService authService, JwtUtil jwtUtil) {
        this.authService = authService;
        this.jwtUtil = jwtUtil;
    }

    @GetMapping("/profile")
    public ResponseEntity<ClienteProfileResponse> getProfile(
            @RequestHeader("Authorization") String authHeader) {
        Long usuarioId = extraerUsuarioId(authHeader);
        Cliente cliente = authService.getClienteById(usuarioId);
        ClienteProfileResponse response = new ClienteProfileResponse(
            cliente.getId(),
            cliente.getNombre(),
            cliente.getEmail(),
            cliente.getTelefono(),
            cliente.getDireccion()
        );
        return ResponseEntity.ok(response);
    }

    @PutMapping("/profile")
    public ResponseEntity<ClienteProfileResponse> updateProfile(
            @RequestHeader("Authorization") String authHeader,
            @RequestBody UpdateProfileRequest request) {
        Long usuarioId = extraerUsuarioId(authHeader);
        Cliente cliente = authService.actualizarPerfil(usuarioId, request);
        ClienteProfileResponse response = new ClienteProfileResponse(
            cliente.getId(),
            cliente.getNombre(),
            cliente.getEmail(),
            cliente.getTelefono(),
            cliente.getDireccion()
        );
        return ResponseEntity.ok(response);
    }

    private Long extraerUsuarioId(String authHeader) {
        String token = authHeader.substring(7);
        return jwtUtil.extractUsuarioId(token);
    }
}
