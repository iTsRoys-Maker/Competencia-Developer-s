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
    @JoinColumn(name = "cliente_id")
    private Cliente cliente;
    
    @OneToMany(mappedBy = "carrito", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<ItemCarrito> items = new ArrayList<>();
    
    public Carrito() {}
    
    public Carrito(Cliente cliente) {
        this.cliente = cliente;
    }
    
    @Override
    public String toString() {
        return String.format("Carrito{id=%d, cliente=%s, items=%d}", 
            id, cliente.getNombre(), items.size());
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
    
    public Cliente getCliente() { return cliente; }
    public void setCliente(Cliente cliente) { this.cliente = cliente; }
    
    public List<ItemCarrito> getItems() { return items; }
    public void setItems(List<ItemCarrito> items) { this.items = items; }
}
