package com.ecommerce.dto;

public class AuthResponse {
    
    private String token;
    private String email;
    private String rol;
    private String nombre;
    private Long usuarioId;
    
    public AuthResponse(String token, String email, String rol, String nombre, Long usuarioId) {
        this.token = token;
        this.email = email;
        this.rol = rol;
        this.nombre = nombre;
        this.usuarioId = usuarioId;
    }
    
    public String getToken() { return token; }
    public void setToken(String token) { this.token = token; }
    
    public String getEmail() { return email; }
    public void setEmail(String email) { this.email = email; }
    
    public String getRol() { return rol; }
    public void setRol(String rol) { this.rol = rol; }
    
    public String getNombre() { return nombre; }
    public void setNombre(String nombre) { this.nombre = nombre; }
    
    public Long getUsuarioId() { return usuarioId; }
    public void setUsuarioId(Long usuarioId) { this.usuarioId = usuarioId; }
}
