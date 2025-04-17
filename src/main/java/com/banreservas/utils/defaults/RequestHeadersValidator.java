package com.banreservas.utils.defaults;

import java.util.List;

import jakarta.ws.rs.core.HttpHeaders;

/**
 * Clase para validar los encabezados de las solicitudes HTTP.
 * Esta clase se encarga de verificar que los encabezados obligatorios estén
 * presentes en la solicitud.
 * Si algún encabezado está ausente o vacío, se devolverá un mensaje de error
 * indicando cuál encabezado falta.
 * Si todos los encabezados son válidos, devuelve la cadena "valid".
 * 
 * @author Ing. Victor Diaz - vjdiaz@banreservas.com
 * @since 06-02-2025
 * @version 1.0
 */
public class RequestHeadersValidator {

    /**
     * Valor que representa una validación exitosa.
     */
    public static final String VALID = "valid";

    /**
     * Valida los encabezados de una solicitud HTTP.
     * Recorre los encabezados definidos en {@link RequestHeaders#HEADERS} y
     * verifica que no falte ninguno
     * y que sus valores no estén vacíos ni sean en blanco.
     * 
     * @param headers Los encabezados de la solicitud HTTP a validar.
     * @return Un mensaje indicando si la validación es exitosa o el encabezado que
     *         falta o es inválido.
     */
    public static String validateRequestHeaders(HttpHeaders headers) {

        // Recorre todos los encabezados definidos en la clase RequestHeaders
        for (String header : RequestHeaders.HEADERS) {
            // Obtiene los valores del encabezado
            List<String> values = headers.getRequestHeaders().get(header);

            // Si el valor está vacío o no existe, retorna un mensaje indicando el
            // encabezado faltante
            if (values == null || values.isEmpty() || values.get(0).isBlank()) {
                return String.format("Header %s es obligatorio", header);
            }
        }

        // Si todos los encabezados son válidos, retorna "valid"
        return VALID;
    }
}
