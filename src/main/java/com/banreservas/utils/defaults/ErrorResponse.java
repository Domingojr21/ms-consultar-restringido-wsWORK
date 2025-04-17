package com.banreservas.utils.defaults;

import io.quarkus.runtime.annotations.RegisterForReflection;

/**
 * Clase para retornar los mensajes de error de las excepciones.
 * 
 * @author Ing. Victor Diaz - vjdiaz@banreservas.com
 * @since 26-01-2025
 * @version 1.0
 */

@RegisterForReflection
public class ErrorResponse {

    public ErrorResponse() {
    }

    public ErrorResponse(String message) {
        this.message = message;
    }

    private String message;

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }
}