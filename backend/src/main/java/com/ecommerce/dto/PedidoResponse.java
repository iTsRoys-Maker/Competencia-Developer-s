package com.ecommerce.dto;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

public class PedidoResponse {

    private Long id;
    private String numeroOrden;
    private String facturaId;
    private LocalDateTime fechaPedido;
    private String estado;
    private BigDecimal total;
    private String metodoPago;
    private String direccionEnvio;
    private Long usuarioId;
    private String usuarioNombre;
    private String usuarioEmail;
    private List<DetallePedidoResponse> detalles;

    public PedidoResponse() {}

    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }

    public String getNumeroOrden() { return numeroOrden; }
    public void setNumeroOrden(String numeroOrden) { this.numeroOrden = numeroOrden; }

    public String getFacturaId() { return facturaId; }
    public void setFacturaId(String facturaId) { this.facturaId = facturaId; }

    public LocalDateTime getFechaPedido() { return fechaPedido; }
    public void setFechaPedido(LocalDateTime fechaPedido) { this.fechaPedido = fechaPedido; }

    public String getEstado() { return estado; }
    public void setEstado(String estado) { this.estado = estado; }

    public BigDecimal getTotal() { return total; }
    public void setTotal(BigDecimal total) { this.total = total; }

    public String getMetodoPago() { return metodoPago; }
    public void setMetodoPago(String metodoPago) { this.metodoPago = metodoPago; }

    public String getDireccionEnvio() { return direccionEnvio; }
    public void setDireccionEnvio(String direccionEnvio) { this.direccionEnvio = direccionEnvio; }

    public Long getUsuarioId() { return usuarioId; }
    public void setUsuarioId(Long usuarioId) { this.usuarioId = usuarioId; }

    public String getUsuarioNombre() { return usuarioNombre; }
    public void setUsuarioNombre(String usuarioNombre) { this.usuarioNombre = usuarioNombre; }

    public String getUsuarioEmail() { return usuarioEmail; }
    public void setUsuarioEmail(String usuarioEmail) { this.usuarioEmail = usuarioEmail; }

    public List<DetallePedidoResponse> getDetalles() { return detalles; }
    public void setDetalles(List<DetallePedidoResponse> detalles) { this.detalles = detalles; }
}
