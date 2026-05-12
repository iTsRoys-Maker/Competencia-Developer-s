package com.ecommerce.controller;

import com.ecommerce.config.JwtUtil;
import com.ecommerce.dto.AddToCartRequest;
import com.ecommerce.dto.CarritoResponse;
import com.ecommerce.service.CarritoService;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/cart")
public class CarritoController {
    
    private final CarritoService carritoService;
    private final JwtUtil jwtUtil;
    
    public CarritoController(CarritoService carritoService, JwtUtil jwtUtil) {
        this.carritoService = carritoService;
        this.jwtUtil = jwtUtil;
    }
    
    @GetMapping
    public ResponseEntity<CarritoResponse> getCarrito(@RequestHeader("Authorization") String authHeader) {
        Long clienteId = extraerUsuarioId(authHeader);
        CarritoResponse carrito = carritoService.getCarritoResponse(clienteId);
        return ResponseEntity.ok(carrito);
    }
    
    @PostMapping("/add")
    public ResponseEntity<CarritoResponse> agregarAlCarrito(
            @RequestHeader("Authorization") String authHeader,
            @Valid @RequestBody AddToCartRequest request) {
        Long clienteId = extraerUsuarioId(authHeader);
        CarritoResponse carrito = carritoService.agregarAlCarrito(clienteId, request);
        return ResponseEntity.ok(carrito);
    }
    
    @DeleteMapping("/remove/{productoId}")
    public ResponseEntity<CarritoResponse> removerDelCarrito(
            @RequestHeader("Authorization") String authHeader,
            @PathVariable Long productoId) {
        Long clienteId = extraerUsuarioId(authHeader);
        CarritoResponse carrito = carritoService.removerDelCarrito(clienteId, productoId);
        return ResponseEntity.ok(carrito);
    }
    
    @DeleteMapping("/clear")
    public ResponseEntity<CarritoResponse> limpiarCarrito(@RequestHeader("Authorization") String authHeader) {
        Long clienteId = extraerUsuarioId(authHeader);
        CarritoResponse carrito = carritoService.limpiarCarrito(clienteId);
        return ResponseEntity.ok(carrito);
    }
    
    private Long extraerUsuarioId(String authHeader) {
        String token = authHeader.substring(7);
        return jwtUtil.extractUsuarioId(token);
    }
}
