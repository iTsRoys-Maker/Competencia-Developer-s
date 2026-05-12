package com.ecommerce.exception;

public class CarritoVacioException extends RuntimeException {
    
    public CarritoVacioException() {
        super("El carrito esta vacio");
    }
    
    public CarritoVacioException(String mensaje) {
        super(mensaje);
    }
}
