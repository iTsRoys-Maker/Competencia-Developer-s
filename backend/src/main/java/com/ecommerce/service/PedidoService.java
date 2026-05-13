package com.ecommerce.service;

import com.ecommerce.dto.CreateOrderRequest;
import com.ecommerce.dto.DetallePedidoResponse;
import com.ecommerce.dto.PedidoResponse;
import com.ecommerce.exception.CarritoVacioException;
import com.ecommerce.exception.RecursoNoEncontradoException;
import com.ecommerce.model.*;
import com.ecommerce.repository.CarritoRepository;
import com.ecommerce.repository.PedidoRepository;
import com.ecommerce.repository.ProductoRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.time.Year;
import java.util.UUID;

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
    public Pedido crearPedido(Long usuarioId, CreateOrderRequest request) {
        carritoService.validarCarrito(usuarioId);
        
        Carrito carrito = carritoRepository.findByUsuarioId(usuarioId)
            .orElseThrow(() -> new RecursoNoEncontradoException("Carrito no encontrado"));
        
        if (carrito.getItems().isEmpty()) {
            throw new CarritoVacioException();
        }
        
        Pago pago = crearPago(request);
        
        if (!pago.procesar(carrito.getTotal())) {
            throw new IllegalStateException("Error al procesar el pago");
        }
        
        Pedido pedido = new Pedido(carrito.getUsuario(), request.getDireccionEnvio());
        pedido.setMetodoPago(pago.getMetodoPago());
        pedido.setNumeroOrden("ORD-" + UUID.randomUUID().toString().substring(0, 8).toUpperCase());
        
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
    
    public List<Pedido> getPedidosPorUsuario(Long usuarioId) {
        return pedidoRepository.findByUsuarioIdOrderByFechaPedidoDesc(usuarioId);
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
        long totalPedidos = pedidoRepository.count();
        long pendientes = pedidoRepository.countByEstadoIn(List.of("PENDIENTE"));
        long completados = pedidoRepository.countByEstadoIn(List.of("COMPLETADO"));
        long cancelados = pedidoRepository.countByEstadoIn(List.of("CANCELADO"));
        
        BigDecimal totalVentas = pedidoRepository.sumTotalByEstadoIn(List.of("COMPLETADO"));
        
        Map<String, Object> stats = new HashMap<>();
        stats.put("totalPedidos", totalPedidos);
        stats.put("pedidosPendientes", pendientes);
        stats.put("pedidosCompletados", completados);
        stats.put("pedidosCancelados", cancelados);
        stats.put("totalVentas", totalVentas);
        
        return stats;
    }

    public PedidoResponse toPedidoResponse(Pedido pedido) {
        PedidoResponse response = new PedidoResponse();
        response.setId(pedido.getId());
        response.setNumeroOrden(pedido.getNumeroOrden());
        response.setFacturaId("FAC-" + Year.now() + "-" + String.format("%04d", pedido.getId()));
        response.setFechaPedido(pedido.getFechaPedido());
        response.setEstado(pedido.getEstado());
        response.setTotal(pedido.getTotal());
        response.setMetodoPago(pedido.getMetodoPago());
        response.setDireccionEnvio(pedido.getDireccionEnvio());

        Usuario usuario = pedido.getUsuario();
        if (usuario != null) {
            response.setUsuarioId(usuario.getId());
            response.setUsuarioNombre(usuario.getNombre());
            response.setUsuarioEmail(usuario.getEmail());
        }

        response.setDetalles(pedido.getDetalles().stream()
            .map(this::toDetalleResponse)
            .collect(Collectors.toList()));

        return response;
    }

    public DetallePedidoResponse toDetalleResponse(DetallePedido detalle) {
        return new DetallePedidoResponse(
            detalle.getId(),
            detalle.getProducto().getId(),
            detalle.getProducto().getNombre(),
            detalle.getCantidad(),
            detalle.getPrecioUnitario(),
            detalle.getSubtotal()
        );
    }

    public List<PedidoResponse> toPedidoResponseList(List<Pedido> pedidos) {
        return pedidos.stream()
            .map(this::toPedidoResponse)
            .collect(Collectors.toList());
    }
}
