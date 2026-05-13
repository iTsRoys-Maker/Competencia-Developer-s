package com.ecommerce.controller;

import com.ecommerce.dto.PedidoResponse;
import com.ecommerce.model.Pedido;
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
        List<Pedido> pedidos;
        
        if (estado != null && !estado.isEmpty()) {
            pedidos = pedidoService.getPedidosPorEstado(estado);
        } else {
            pedidos = pedidoService.getTodosLosPedidos();
        }
        
        return ResponseEntity.ok(pedidoService.toPedidoResponseList(pedidos));
    }
    
    @PutMapping("/orders/{id}/status")
    public ResponseEntity<PedidoResponse> actualizarEstadoPedido(
            @PathVariable Long id,
            @RequestBody Map<String, String> body) {
        String nuevoEstado = body.get("estado");
        Pedido pedido = pedidoService.actualizarEstadoPedido(id, nuevoEstado);
        return ResponseEntity.ok(pedidoService.toPedidoResponse(pedido));
    }
    
    @GetMapping("/stats")
    public ResponseEntity<Map<String, Object>> getEstadisticas() {
        Map<String, Object> stats = pedidoService.getEstadisticas();
        return ResponseEntity.ok(stats);
    }
    
    @PostMapping("/products")
    public ResponseEntity<Producto> crearProducto(@RequestBody Producto producto) {
        Producto nuevoProducto = productoService.crearProducto(producto);
        return ResponseEntity.ok(nuevoProducto);
    }
    
    @PutMapping("/products/{id}")
    public ResponseEntity<Producto> actualizarProducto(
            @PathVariable Long id,
            @RequestBody Producto producto) {
        Producto productoActualizado = productoService.actualizarProducto(id, producto);
        return ResponseEntity.ok(productoActualizado);
    }
    
    @DeleteMapping("/products/{id}")
    public ResponseEntity<Void> eliminarProducto(@PathVariable Long id) {
        productoService.eliminarProducto(id);
        return ResponseEntity.noContent().build();
    }
}
