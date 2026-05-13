package com.ecommerce.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "pedidos")
public class Pedido {
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    
    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "usuario_id")
    private Usuario usuario;

    // Desnormalizado: se guarda al crear el pedido para no depender de joins
    @Column(name = "cliente_nombre")
    private String clienteNombre;

    @Column(name = "cliente_email")
    private String clienteEmail;
    
    @Column(name = "numero_orden", unique = true)
    private String numeroOrden;
    
    @Column(name = "fecha_pedido")
    private LocalDateTime fechaPedido;
    
    @Column(nullable = false)
    private String estado;
    
    @Column(name = "total", nullable = false, precision = 10, scale = 2)
    private BigDecimal total;
    
    @Column(name = "metodo_pago")
    private String metodoPago;
    
    @Column(name = "direccion_envio")
    private String direccionEnvio;
    
    @OneToMany(mappedBy = "pedido", cascade = CascadeType.ALL, orphanRemoval = true, fetch = FetchType.EAGER)
    private List<DetallePedido> detalles = new ArrayList<>();
    
    public Pedido() {}
    
    public Pedido(Usuario usuario, String direccionEnvio) {
        this.usuario = usuario;
        // Guardar nombre y email en el momento de la creación
        if (usuario != null) {
            this.clienteNombre = usuario.getNombre();
            this.clienteEmail  = usuario.getEmail();
        }
        this.fechaPedido = LocalDateTime.now();
        this.estado = "PENDIENTE";
        this.direccionEnvio = direccionEnvio;
    }
    
    @Override
    public String toString() {
        return String.format("Pedido{id=%d, numeroOrden='%s', cliente=%s, estado=%s, total=%s}", 
            id, numeroOrden, clienteNombre, estado, total);
    }
    
    public void agregarDetalle(Producto producto, int cantidad, BigDecimal precioUnitario) {
        DetallePedido detalle = new DetallePedido(this, producto, cantidad, precioUnitario);
        detalles.add(detalle);
    }
    
    public void calcularTotal() {
        this.total = detalles.stream()
            .map(DetallePedido::getSubtotal)
            .reduce(BigDecimal.ZERO, BigDecimal::add);
    }
    
    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }
    
    @JsonIgnore
    public Usuario getUsuario() { return usuario; }
    public void setUsuario(Usuario usuario) { this.usuario = usuario; }

    public String getClienteNombre() { return clienteNombre; }
    public void setClienteNombre(String clienteNombre) { this.clienteNombre = clienteNombre; }

    public String getClienteEmail() { return clienteEmail; }
    public void setClienteEmail(String clienteEmail) { this.clienteEmail = clienteEmail; }
    
    public String getNumeroOrden() { return numeroOrden; }
    public void setNumeroOrden(String numeroOrden) { this.numeroOrden = numeroOrden; }
    
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
    
    public List<DetallePedido> getDetalles() { return detalles; }
    public void setDetalles(List<DetallePedido> detalles) { this.detalles = detalles; }
}
