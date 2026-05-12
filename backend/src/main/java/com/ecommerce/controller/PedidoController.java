package com.ecommerce.controller;

import com.ecommerce.config.JwtUtil;
import com.ecommerce.dto.CreateOrderRequest;
import com.ecommerce.model.Pedido;
import com.ecommerce.service.PedidoService;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/orders")
public class PedidoController {
    
    private final PedidoService pedidoService;
    private final JwtUtil jwtUtil;
    
    public PedidoController(PedidoService pedidoService, JwtUtil jwtUtil) {
        this.pedidoService = pedidoService;
        this.jwtUtil = jwtUtil;
    }
    
    @PostMapping
    public ResponseEntity<Pedido> crearPedido(
            @RequestHeader("Authorization") String authHeader,
            @Valid @RequestBody CreateOrderRequest request) {
        Long clienteId = extraerUsuarioId(authHeader);
        Pedido pedido = pedidoService.crearPedido(clienteId, request);
        return ResponseEntity.ok(pedido);
    }
    
    @GetMapping("/my-orders")
    public ResponseEntity<List<Pedido>> getMisPedidos(@RequestHeader("Authorization") String authHeader) {
        Long clienteId = extraerUsuarioId(authHeader);
        List<Pedido> pedidos = pedidoService.getPedidosPorCliente(clienteId);
        return ResponseEntity.ok(pedidos);
    }
    
    @GetMapping("/{id}")
    public ResponseEntity<Pedido> getPedidoById(@PathVariable Long id) {
        Pedido pedido = pedidoService.getPedidoPorId(id);
        return ResponseEntity.ok(pedido);
    }
    
    private Long extraerUsuarioId(String authHeader) {
        String token = authHeader.substring(7);
        return jwtUtil.extractUsuarioId(token);
    }
}
