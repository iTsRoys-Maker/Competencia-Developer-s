package com.ecommerce.model;

import java.math.BigDecimal;

public class PagoTarjeta implements Pago {
    
    private String numeroTarjeta;
    private String titular;
    private String cvv;
    private String fechaVencimiento;
    
    public PagoTarjeta() {}
    
    public PagoTarjeta(String numeroTarjeta, String titular, String cvv, String fechaVencimiento) {
        this.numeroTarjeta = numeroTarjeta;
        this.titular = titular;
        this.cvv = cvv;
        this.fechaVencimiento = fechaVencimiento;
    }
    
    @Override
    public boolean procesar(BigDecimal monto) {
        if (!validarTarjeta()) {
            return false;
        }
        return monto.compareTo(BigDecimal.ZERO) > 0;
    }
    
    @Override
    public String getMetodoPago() {
        return "TARJETA";
    }
    
    @Override
    public String getDescripcion() {
        return String.format("Pago con tarjeta terminada en %s", 
            numeroTarjeta != null && numeroTarjeta.length() >= 4 ? 
            numeroTarjeta.substring(numeroTarjeta.length() - 4) : "****");
    }
    
    private boolean validarTarjeta() {
        return numeroTarjeta != null && numeroTarjeta.length() >= 13 &&
               cvv != null && cvv.length() >= 3 &&
               titular != null && !titular.isEmpty();
    }
    
    public String getNumeroTarjeta() { return numeroTarjeta; }
    public void setNumeroTarjeta(String numeroTarjeta) { this.numeroTarjeta = numeroTarjeta; }
    
    public String getTitular() { return titular; }
    public void setTitular(String titular) { this.titular = titular; }
    
    public String getCvv() { return cvv; }
    public void setCvv(String cvv) { this.cvv = cvv; }
    
    public String getFechaVencimiento() { return fechaVencimiento; }
    public void setFechaVencimiento(String fechaVencimiento) { this.fechaVencimiento = fechaVencimiento; }
}
