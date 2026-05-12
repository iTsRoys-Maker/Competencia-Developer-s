package com.ecommerce.dto;

import java.math.BigDecimal;
import java.util.List;

public class CarritoResponse {
    
    private Long id;
    private String clienteNombre;
    private List<ItemCarritoResponse> items;
    private BigDecimal total;
    private int cantidadTotal;
    
    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }
    
    public String getClienteNombre() { return clienteNombre; }
    public void setClienteNombre(String clienteNombre) { this.clienteNombre = clienteNombre; }
    
    public List<ItemCarritoResponse> getItems() { return items; }
    public void setItems(List<ItemCarritoResponse> items) { this.items = items; }
    
    public BigDecimal getTotal() { return total; }
    public void setTotal(BigDecimal total) { this.total = total; }
    
    public int getCantidadTotal() { return cantidadTotal; }
    public void setCantidadTotal(int cantidadTotal) { this.cantidadTotal = cantidadTotal; }
    
    public static class ItemCarritoResponse {
        private Long productoId;
        private String productoNombre;
        private BigDecimal precioUnitario;
        private Integer cantidad;
        private BigDecimal subtotal;
        
        public Long getProductoId() { return productoId; }
        public void setProductoId(Long productoId) { this.productoId = productoId; }
        
        public String getProductoNombre() { return productoNombre; }
        public void setProductoNombre(String productoNombre) { this.productoNombre = productoNombre; }
        
        public BigDecimal getPrecioUnitario() { return precioUnitario; }
        public void setPrecioUnitario(BigDecimal precioUnitario) { this.precioUnitario = precioUnitario; }
        
        public Integer getCantidad() { return cantidad; }
        public void setCantidad(Integer cantidad) { this.cantidad = cantidad; }
        
        public BigDecimal getSubtotal() { return subtotal; }
        public void setSubtotal(BigDecimal subtotal) { this.subtotal = subtotal; }
    }
}
