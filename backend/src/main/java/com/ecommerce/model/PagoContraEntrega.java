package com.ecommerce.model;

import java.math.BigDecimal;

public class PagoContraEntrega implements Pago {
    
    private String nombreReceptor;
    private String telefonoContacto;
    
    public PagoContraEntrega() {}
    
    public PagoContraEntrega(String nombreReceptor, String telefonoContacto) {
        this.nombreReceptor = nombreReceptor;
        this.telefonoContacto = telefonoContacto;
    }
    
    @Override
    public boolean procesar(BigDecimal monto) {
        return validarDatos() && monto.compareTo(BigDecimal.ZERO) > 0;
    }
    
    @Override
    public String getMetodoPago() {
        return "CONTRA_ENTREGA";
    }
    
    @Override
    public String getDescripcion() {
        return String.format("Pago contra entrega - Receptor: %s", nombreReceptor);
    }
    
    private boolean validarDatos() {
        return nombreReceptor != null && !nombreReceptor.isEmpty() &&
               telefonoContacto != null && !telefonoContacto.isEmpty();
    }
    
    public String getNombreReceptor() { return nombreReceptor; }
    public void setNombreReceptor(String nombreReceptor) { this.nombreReceptor = nombreReceptor; }
    
    public String getTelefonoContacto() { return telefonoContacto; }
    public void setTelefonoContacto(String telefonoContacto) { this.telefonoContacto = telefonoContacto; }
}
