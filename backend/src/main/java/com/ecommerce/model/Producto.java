package com.ecommerce.model;

import jakarta.persistence.*;
import java.math.BigDecimal;

@Entity
@Table(name = "productos")
public class Producto {
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    
    @Column(nullable = false)
    private String nombre;
    
    @Column(length = 1000)
    private String descripcion;
    
    @Column(nullable = false, precision = 10, scale = 2)
    private BigDecimal precio;
    
    @Column(nullable = false)
    private Integer stock;
    
    @Column(name = "imagen_url")
    private String imagenUrl;
    
    @Column(name = "categoria")
    private String categoria;
    
    public Producto() {}
    
    public Producto(String nombre, String descripcion, BigDecimal precio, Integer stock, String imagenUrl, String categoria) {
        this.nombre = nombre;
        this.descripcion = descripcion;
        this.precio = precio;
        this.stock = stock;
        this.imagenUrl = imagenUrl;
        this.categoria = categoria;
    }
    
    @Override
    public String toString() {
        return String.format("Producto{id=%d, nombre='%s', precio=%s, stock=%d}", 
            id, nombre, precio, stock);
    }
    
    public boolean tieneStock() {
        return stock != null && stock > 0;
    }
    
    public boolean tieneStockSuficiente(int cantidad) {
        return stock != null && stock >= cantidad;
    }
    
    public void reducirStock(int cantidad) {
        if (tieneStockSuficiente(cantidad)) {
            this.stock -= cantidad;
        }
    }
    
    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }
    
    public String getNombre() { return nombre; }
    public void setNombre(String nombre) { this.nombre = nombre; }
    
    public String getDescripcion() { return descripcion; }
    public void setDescripcion(String descripcion) { this.descripcion = descripcion; }
    
    public BigDecimal getPrecio() { return precio; }
    public void setPrecio(BigDecimal precio) { this.precio = precio; }
    
    public Integer getStock() { return stock; }
    public void setStock(Integer stock) { this.stock = stock; }
    
    public String getImagenUrl() { return imagenUrl; }
    public void setImagenUrl(String imagenUrl) { this.imagenUrl = imagenUrl; }
    
    public String getCategoria() { return categoria; }
    public void setCategoria(String categoria) { this.categoria = categoria; }
}
