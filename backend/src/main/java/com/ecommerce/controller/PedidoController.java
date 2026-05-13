package com.ecommerce.controller;

import com.ecommerce.config.JwtUtil;
import com.ecommerce.dto.CreateOrderRequest;
import com.ecommerce.dto.PedidoResponse;
import com.ecommerce.service.PedidoService;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/orders")
@CrossOrigin(origins = "*", allowedHeaders = "*")
public class PedidoController {
    
    private final PedidoService pedidoService;
    private final JwtUtil jwtUtil;
    
    public PedidoController(PedidoService pedidoService, JwtUtil jwtUtil) {
        this.pedidoService = pedidoService;
        this.jwtUtil = jwtUtil;
    }
    
    @PostMapping
    public ResponseEntity<PedidoResponse> crearPedido(
            @RequestHeader("Authorization") String authHeader,
            @Valid @RequestBody CreateOrderRequest request) {
        Long usuarioId = extraerUsuarioId(authHeader);
        return ResponseEntity.ok(pedidoService.crearPedido(usuarioId, request));
    }
    
    @GetMapping("/my-orders")
    public ResponseEntity<List<PedidoResponse>> getMisPedidos(
            @RequestHeader("Authorization") String authHeader) {
        Long usuarioId = extraerUsuarioId(authHeader);
        return ResponseEntity.ok(pedidoService.getPedidosPorUsuario(usuarioId));
    }
    
    @GetMapping("/{id}")
    public ResponseEntity<PedidoResponse> getPedidoById(@PathVariable Long id) {
        return ResponseEntity.ok(pedidoService.getPedidoPorId(id));
    }
    
    private Long extraerUsuarioId(String authHeader) {
        String token = authHeader.substring(7);
        return jwtUtil.extractUsuarioId(token);
    }
}
