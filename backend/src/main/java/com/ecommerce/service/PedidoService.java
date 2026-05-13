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
    public PedidoResponse crearPedido(Long usuarioId, CreateOrderRequest request) {
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

        // Recargar con JOIN FETCH para tener todos los datos en la respuesta
        return buildResponse(pedidoRepository.findByIdWithDetails(pedido.getId()).orElse(pedido));
    }
    
    @Transactional(readOnly = true)
    public List<PedidoResponse> getPedidosPorUsuario(Long usuarioId) {
        return pedidoRepository.findByUsuarioIdOrderByFechaPedidoDesc(usuarioId)
                .stream().map(this::buildResponse).collect(Collectors.toList());
    }
    
    @Transactional(readOnly = true)
    public List<PedidoResponse> getTodosLosPedidos() {
        return pedidoRepository.findAllByOrderByFechaPedidoDesc()
                .stream().map(this::buildResponse).collect(Collectors.toList());
    }
    
    @Transactional(readOnly = true)
    public List<PedidoResponse> getPedidosPorEstado(String estado) {
        return pedidoRepository.findByEstadoOrderByFechaPedidoDesc(estado)
                .stream().map(this::buildResponse).collect(Collectors.toList());
    }
    
    @Transactional
    public PedidoResponse actualizarEstadoPedido(Long pedidoId, String nuevoEstado) {
        Pedido pedido = pedidoRepository.findByIdWithDetails(pedidoId)
            .orElseThrow(() -> new RecursoNoEncontradoException("Pedido", pedidoId));
        pedido.setEstado(nuevoEstado);
        pedido = pedidoRepository.save(pedido);
        return buildResponse(pedido);
    }
    
    @Transactional(readOnly = true)
    public PedidoResponse getPedidoPorId(Long id) {
        Pedido pedido = pedidoRepository.findByIdWithDetails(id)
            .orElseThrow(() -> new RecursoNoEncontradoException("Pedido", id));
        return buildResponse(pedido);
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

    /**
     * Construye el DTO de respuesta a partir de un Pedido ya cargado con JOIN FETCH.
     * Todos los datos (usuario, detalles, productos) están disponibles directamente.
     */
    private PedidoResponse buildResponse(Pedido pedido) {
        PedidoResponse response = new PedidoResponse();
        response.setId(pedido.getId());
        response.setNumeroOrden(pedido.getNumeroOrden());
        response.setFacturaId("FAC-" + Year.now() + "-" + String.format("%04d", pedido.getId()));
        response.setFechaPedido(pedido.getFechaPedido());
        response.setEstado(pedido.getEstado());
        response.setTotal(pedido.getTotal());
        response.setMetodoPago(pedido.getMetodoPago());
        response.setDireccionEnvio(pedido.getDireccionEnvio());

        // Usar campos desnormalizados como prioridad (garantizan que siempre haya datos)
        String nombreUsuario = pedido.getClienteNombre();
        String emailUsuario = pedido.getClienteEmail();
        
        Usuario usuario = pedido.getUsuario();
        if (usuario != null) {
            response.setUsuarioId(usuario.getId());
            if (nombreUsuario == null) nombreUsuario = usuario.getNombre();
            if (emailUsuario == null) emailUsuario = usuario.getEmail();
        }
        
        response.setUsuarioNombre(nombreUsuario != null ? nombreUsuario : "Cliente Desconocido");
        response.setUsuarioEmail(emailUsuario != null ? emailUsuario : "Sin email");

        // Detalles + productos: usar campo desnormalizado productoNombre
        List<DetallePedidoResponse> detalles = pedido.getDetalles().stream()
            .map(d -> {
                Long productoId = d.getProducto() != null ? d.getProducto().getId() : null;
                String productoNombre = d.getProductoNombre();
                if (productoNombre == null && d.getProducto() != null) {
                    productoNombre = d.getProducto().getNombre();
                }
                if (productoNombre == null) {
                    productoNombre = "Producto no disponible";
                }
                
                return new DetallePedidoResponse(
                    d.getId(),
                    productoId,
                    productoNombre,
                    d.getCantidad(),
                    d.getPrecioUnitario(),
                    d.getSubtotal()
                );
            })
            .collect(Collectors.toList());
        response.setDetalles(detalles);

        return response;
    }

    // ── Métodos legacy mantenidos para compatibilidad con PedidoController ──

    public PedidoResponse toPedidoResponse(Pedido pedido) {
        return buildResponse(pedido);
    }

    public List<PedidoResponse> toPedidoResponseList(List<Pedido> pedidos) {
        return pedidos.stream().map(this::buildResponse).collect(Collectors.toList());
    }
}
