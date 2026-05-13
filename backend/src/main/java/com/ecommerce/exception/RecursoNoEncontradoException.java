package com.ecommerce.exception;

public class RecursoNoEncontradoException extends RuntimeException {
    
    public RecursoNoEncontradoException(String mensaje) {
        super(mensaje);
    }
    
    public RecursoNoEncontradoException(String recurso, Long id) {
        super(String.format("%s con id %d no encontrado", recurso, id));
    }
}
