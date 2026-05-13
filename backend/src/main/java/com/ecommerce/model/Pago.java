package com.ecommerce.model;

import java.math.BigDecimal;

public interface Pago {
    
    boolean procesar(BigDecimal monto);
    
    String getMetodoPago();
    
    default String getDescripcion() {
        return "Metodo de pago: " + getMetodoPago();
    }
}
