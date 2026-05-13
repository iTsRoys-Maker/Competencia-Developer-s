package com.ecommerce.controller;

import com.ecommerce.dto.PedidoResponse;
import com.ecommerce.model.Producto;
import com.ecommerce.service.PedidoService;
import com.ecommerce.service.ProductoService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/admin")
@CrossOrigin(origins = "*", allowedHeaders = "*")
public class AdminController {
    
    private final PedidoService pedidoService;
    private final ProductoService productoService;
    
    public AdminController(PedidoService pedidoService, ProductoService productoService) {
        this.pedidoService = pedidoService;
        this.productoService = productoService;
    }
    
    @GetMapping("/orders")
    public ResponseEntity<List<PedidoResponse>> getTodosLosPedidos(
            @RequestParam(required = false) String estado) {
        if (estado != null && !estado.isEmpty()) {
            return ResponseEntity.ok(pedidoService.getPedidosPorEstado(estado));
        }
        return ResponseEntity.ok(pedidoService.getTodosLosPedidos());
    }
    
    @PutMapping("/orders/{id}/status")
    public ResponseEntity<PedidoResponse> actualizarEstadoPedido(
            @PathVariable Long id,
            @RequestBody Map<String, String> body) {
        String nuevoEstado = body.get("estado");
        return ResponseEntity.ok(pedidoService.actualizarEstadoPedido(id, nuevoEstado));
    }
    
    @GetMapping("/stats")
    public ResponseEntity<Map<String, Object>> getEstadisticas() {
        return ResponseEntity.ok(pedidoService.getEstadisticas());
    }
    
    @PostMapping("/products")
    public ResponseEntity<Producto> crearProducto(@RequestBody Producto producto) {
        return ResponseEntity.ok(productoService.crearProducto(producto));
    }
    
    @PutMapping("/products/{id}")
    public ResponseEntity<Producto> actualizarProducto(
            @PathVariable Long id,
            @RequestBody Producto producto) {
        return ResponseEntity.ok(productoService.actualizarProducto(id, producto));
    }
    
    @DeleteMapping("/products/{id}")
    public ResponseEntity<Void> eliminarProducto(@PathVariable Long id) {
        productoService.eliminarProducto(id);
        return ResponseEntity.noContent().build();
    }
}
