package com.ecommerce.exception;

public class StockInsuficienteException extends RuntimeException {
    
    public StockInsuficienteException(String producto, int disponible, int solicitado) {
        super(String.format("Stock insuficiente para %s. Disponible: %d, Solicitado: %d", 
            producto, disponible, solicitado));
    }
    
    public StockInsuficienteException(String mensaje) {
        super(mensaje);
    }
}
