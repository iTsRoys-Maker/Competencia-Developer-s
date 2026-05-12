package com.ecommerce.model;

import jakarta.persistence.*;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "clientes")
public class Cliente extends Usuario {
    
    @Column(name = "telefono")
    private String telefono;
    
    @Column(name = "direccion")
    private String direccion;
    
    @OneToMany(mappedBy = "cliente", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Pedido> pedidos = new ArrayList<>();
    
    public Cliente() {
        super();
    }
    
    public Cliente(String nombre, String email, String password, String telefono, String direccion) {
        super(nombre, email, password, "CLIENTE");
        this.telefono = telefono;
        this.direccion = direccion;
    }
    
    @Override
    public String getTipoUsuario() {
        return "Cliente";
    }
    
    @Override
    public String toString() {
        return String.format("Cliente{id=%d, nombre='%s', email='%s', telefono='%s'}", 
            getId(), getNombre(), getEmail(), telefono);
    }
    
    public String getTelefono() { return telefono; }
    public void setTelefono(String telefono) { this.telefono = telefono; }
    
    public String getDireccion() { return direccion; }
    public void setDireccion(String direccion) { this.direccion = direccion; }
    
    public List<Pedido> getPedidos() { return pedidos; }
    public void setPedidos(List<Pedido> pedidos) { this.pedidos = pedidos; }
}
