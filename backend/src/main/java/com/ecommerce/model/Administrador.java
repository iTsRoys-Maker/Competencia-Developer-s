package com.ecommerce.model;

import jakarta.persistence.*;

@Entity
@Table(name = "administradores")
public class Administrador extends Usuario {
    
    @Column(name = "departamento")
    private String departamento;
    
    public Administrador() {
        super();
    }
    
    public Administrador(String nombre, String email, String password, String departamento) {
        super(nombre, email, password, "ADMIN");
        this.departamento = departamento;
    }
    
    @Override
    public String getTipoUsuario() {
        return "Administrador";
    }
    
    @Override
    public String toString() {
        return String.format("Administrador{id=%d, nombre='%s', email='%s', departamento='%s'}", 
            getId(), getNombre(), getEmail(), departamento);
    }
    
    public String getDepartamento() { return departamento; }
    public void setDepartamento(String departamento) { this.departamento = departamento; }
}
