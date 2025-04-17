package com.banreservas.utils.defaults;

/**
 * Clase para centralizar mensajes de respuesta asociados a códigos de estado
 * HTTP.
 * 
 * @author Ing. Victor Diaz - vjdiaz@banreservas.com
 * @since 26-01-2025
 * @version 1.0
 */

public class CodeMessages {
    // Mensaje para respuestas exitosas
    public static final String MESSAGE_SUCCESS = "Exitoso";

    // Mensaje para solicitudes incorrectas
    public static final String MESSAGE_BAD_REQUEST = "Solicitud incorrecta";

    // Mensaje para credenciales inválidas
    public static final String MESSAGE_UNAUTHORIZED = "Credenciales inválidas";

    // Mensaje para acceso prohibido
    public static final String MESSAGE_FORBIDDEN = "No tiene permiso para acceder a este recurso";

    // Mensaje para recursos no encontrados
    public static final String MESSAGE_NOT_FOUND = "No existen datos para esta consulta";

    // Mensaje para errores internos del servidor
    public static final String MESSAGE_INTERNAL_SERVER_ERROR = "Transacción no pudo ser procesada. Intente nuevamente";
}
