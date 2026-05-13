package com.ecommerce.dto;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;

public class AddToCartRequest {
    
    @NotNull(message = "El id del producto es requerido")
    private Long productoId;
    
    @NotNull(message = "La cantidad es requerida")
    @Min(value = 1, message = "La cantidad minima es 1")
    private Integer cantidad;
    
    public Long getProductoId() { return productoId; }
    public void setProductoId(Long productoId) { this.productoId = productoId; }
    
    public Integer getCantidad() { return cantidad; }
    public void setCantidad(Integer cantidad) { this.cantidad = cantidad; }
}
