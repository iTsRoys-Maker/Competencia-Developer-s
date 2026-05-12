package com.ecommerce.service;

import com.ecommerce.dto.CreateOrderRequest;
import com.ecommerce.exception.CarritoVacioException;
import com.ecommerce.exception.RecursoNoEncontradoException;
import com.ecommerce.model.*;
import com.ecommerce.repository.CarritoRepository;
import com.ecommerce.repository.PedidoRepository;
import com.ecommerce.repository.ProductoRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
public class PedidoService {
    
    private final PedidoRepository pedidoRepository;
    private final CarritoRepository carritoRepository;
    private final ProductoRepository productoRepository;
    private final CarritoService carritoService;
    
    public PedidoService(PedidoRepository pedidoRepository,
                         CarritoRepository carritoRepository,
                         ProductoRepository productoRepository,
                         CarritoService carritoService) {
        this.pedidoRepository = pedidoRepository;
        this.carritoRepository = carritoRepository;
        this.productoRepository = productoRepository;
        this.carritoService = carritoService;
    }
    
    @Transactional
    public Pedido crearPedido(Long clienteId, CreateOrderRequest request) {
        carritoService.validarCarrito(clienteId);
        
        Carrito carrito = carritoRepository.findByClienteId(clienteId)
            .orElseThrow(() -> new RecursoNoEncontradoException("Carrito no encontrado"));
        
        if (carrito.getItems().isEmpty()) {
            throw new CarritoVacioException();
        }
        
        Pago pago = crearPago(request);
        
        if (!pago.procesar(carrito.getTotal())) {
            throw new IllegalStateException("Error al procesar el pago");
        }
        
        Pedido pedido = new Pedido(carrito.getCliente(), request.getDireccionEnvio());
        pedido.setMetodoPago(pago.getMetodoPago());
        
        for (ItemCarrito item : carrito.getItems()) {
            Producto producto = item.getProducto();
            
            pedido.agregarDetalle(producto, item.getCantidad(), producto.getPrecio());
            
            producto.reducirStock(item.getCantidad());
            productoRepository.save(producto);
        }
        
        pedido.calcularTotal();
        pedido = pedidoRepository.save(pedido);
        
        carrito.limpiar();
        carritoRepository.save(carrito);
        
        return pedido;
    }
    
    public List<Pedido> getPedidosPorCliente(Long clienteId) {
        return pedidoRepository.findByClienteIdOrderByFechaPedidoDesc(clienteId);
    }
    
    public List<Pedido> getTodosLosPedidos() {
        return pedidoRepository.findAllByOrderByFechaPedidoDesc();
    }
    
    public List<Pedido> getPedidosPorEstado(String estado) {
        return pedidoRepository.findByEstadoOrderByFechaPedidoDesc(estado);
    }
    
    @Transactional
    public Pedido actualizarEstadoPedido(Long pedidoId, String nuevoEstado) {
        Pedido pedido = pedidoRepository.findById(pedidoId)
            .orElseThrow(() -> new RecursoNoEncontradoException("Pedido", pedidoId));
        
        pedido.setEstado(nuevoEstado);
        return pedidoRepository.save(pedido);
    }
    
    public Pedido getPedidoPorId(Long id) {
        return pedidoRepository.findById(id)
            .orElseThrow(() -> new RecursoNoEncontradoException("Pedido", id));
    }
    
    private Pago crearPago(CreateOrderRequest request) {
        Map<String, Pago> pagos = new HashMap<>();
        
        pagos.put("TARJETA", new PagoTarjeta(
            request.getNumeroTarjeta(),
            request.getTitular(),
            request.getCvv(),
            request.getFechaVencimiento()
        ));
        
        pagos.put("CONTRA_ENTREGA", new PagoContraEntrega(
            request.getNombreReceptor(),
            request.getTelefonoContacto()
        ));
        
        Pago pago = pagos.get(request.getMetodoPago());
        
        if (pago == null) {
            throw new IllegalArgumentException("Metodo de pago no valido: " + request.getMetodoPago());
        }
        
        return pago;
    }
    
    public Map<String, Object> getEstadisticas() {
        // Consultas optimizadas con JPA aggregate en lugar de filtrar en memoria
        long totalPedidos = pedidoRepository.count();
        long pendientes = pedidoRepository.countByEstadoIn(List.of("PENDIENTE"));
        long completados = pedidoRepository.countByEstadoIn(List.of("COMPLETADO"));
        long cancelados = pedidoRepository.countByEstadoIn(List.of("CANCELADO"));
        
        // Total ventas: solo pedidos COMPLETADO (y PAGADO si existiera)
        BigDecimal totalVentas = pedidoRepository.sumTotalByEstadoIn(List.of("COMPLETADO"));
        
        Map<String, Object> stats = new HashMap<>();
        stats.put("totalPedidos", totalPedidos);
        stats.put("pedidosPendientes", pendientes);
        stats.put("pedidosCompletados", completados);
        stats.put("pedidosCancelados", cancelados);
        stats.put("totalVentas", totalVentas);
        
        return stats;
    }
}
