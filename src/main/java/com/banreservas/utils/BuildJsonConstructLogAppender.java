package com.banreservas.utils;

import com.fasterxml.jackson.databind.ObjectMapper;

/**
 * Utilidad para convertir objetos en cadenas JSON para su registro en logs.
 * Esta clase proporciona un método estático para convertir cualquier objeto
 * Java en su
 * representación JSON, facilitando la integración de datos estructurados en
 * registros.
 * 
 * @author Ing. Victor Diaz - vjdiaz@banreservas.com
 * @since 06-02-2025
 * @version 1.0
 */
public class BuildJsonConstructLogAppender {

    // Instancia de ObjectMapper para realizar la conversión de objetos a JSON.
    private static final ObjectMapper MAPPER = new ObjectMapper();

    /**
     * Convierte un objeto Java en su representación JSON en formato de cadena.
     * Si ocurre algún error durante la conversión, se devuelve `null`.
     *
     * @param objectJson El objeto que se va a convertir a JSON.
     * @param <T>        El tipo del objeto que se convertirá.
     * @return Una cadena JSON que representa el objeto, o `null` si ocurre un error
     *         durante la conversión.
     */
    public static <T> String buildJson(T objectJson) {
        try {
            // Convierte el objeto en una cadena JSON usando ObjectMapper.
            return MAPPER.writeValueAsString(objectJson);
        } catch (Exception e) {
            // Si ocurre un error, devuelve null.
            return null;
        }
    }

    /**
     * Constructor privado para evitar la instanciación de la clase.
     * Esta clase solo tiene métodos estáticos, por lo que no se permite crear
     * instancias.
     */
    private BuildJsonConstructLogAppender() {
    }
}
