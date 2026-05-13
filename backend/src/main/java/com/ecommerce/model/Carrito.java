package com.ecommerce.model;

import jakarta.persistence.*;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "carritos")
public class Carrito {
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    
    @OneToOne
    @JoinColumn(name = "usuario_id")
    private Usuario usuario;
    
    @OneToMany(mappedBy = "carrito", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<ItemCarrito> items = new ArrayList<>();
    
    public Carrito() {}
    
    public Carrito(Usuario usuario) {
        this.usuario = usuario;
    }
    
    @Override
    public String toString() {
        return String.format("Carrito{id=%d, usuario=%s, items=%d}", 
            id, usuario.getNombre(), items.size());
    }
    
    public void agregarItem(Producto producto, int cantidad) {
        for (ItemCarrito item : items) {
            if (item.getProducto().getId().equals(producto.getId())) {
                item.setCantidad(item.getCantidad() + cantidad);
                return;
            }
        }
        ItemCarrito nuevoItem = new ItemCarrito(this, producto, cantidad);
        items.add(nuevoItem);
    }
    
    public void removerItem(Long productoId) {
        items.removeIf(item -> item.getProducto().getId().equals(productoId));
    }
    
    public void limpiar() {
        items.clear();
    }
    
    public BigDecimal getTotal() {
        return items.stream()
            .map(ItemCarrito::getSubtotal)
            .reduce(BigDecimal.ZERO, BigDecimal::add);
    }
    
    public int getCantidadTotal() {
        return items.stream()
            .mapToInt(ItemCarrito::getCantidad)
            .sum();
    }
    
    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }
    
    public Usuario getUsuario() { return usuario; }
    public void setUsuario(Usuario usuario) { this.usuario = usuario; }
    
    public List<ItemCarrito> getItems() { return items; }
    public void setItems(List<ItemCarrito> items) { this.items = items; }
}
