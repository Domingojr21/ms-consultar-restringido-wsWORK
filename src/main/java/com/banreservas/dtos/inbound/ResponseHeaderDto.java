package com.banreservas.dtos.inbound;

import io.quarkus.runtime.annotations.RegisterForReflection;

/**
 * DTO (Data Transfer Object) que representa el encabezado de una respuesta.
 * Esta clase contiene dos propiedades:
 * - {@link #responseCode}: Un código numérico que representa el código de
 * respuesta de la solicitud.
 * - {@link #responseMessage}: Un mensaje asociado con el código de respuesta.
 * 
 * La clase está registrada para reflexión a través de la anotación
 * {@link RegisterForReflection}
 * para permitir la serialización y deserialización con Quarkus.
 * 
 * Se proporcionan dos constructores:
 * - Un constructor por defecto.
 * - Un constructor que acepta tanto el código de respuesta como el mensaje.
 * 
 * @author Ing. Domingo Ruiz - c-djruiz@banreservas.com
 * @since 14-04-2025
 * @version 1.0
 */

@RegisterForReflection
public class ResponseHeaderDto {

    private int responseCode;
    private String responseMessage;

    /**
     * Constructor por defecto.
     */
    public ResponseHeaderDto() {
    }

    /**
     * Constructor que acepta un código de respuesta y un mensaje.
     * 
     * @param responseCode    El código de respuesta.
     * @param responseMessage El mensaje de respuesta.
     */
    public ResponseHeaderDto(int responseCode, String responseMessage) {
        this.responseCode = responseCode;
        this.responseMessage = responseMessage;
    }

    /**
     * Obtiene el código de respuesta.
     * 
     * @return El código de respuesta.
     */
    public int getResponseCode() {
        return responseCode;
    }

    /**
     * Establece el código de respuesta.
     * 
     * @param responseCode El código de respuesta.
     */
    public void setResponseCode(int responseCode) {
        this.responseCode = responseCode;
    }

    /**
     * Obtiene el mensaje de respuesta.
     * 
     * @return El mensaje de respuesta.
     */
    public String getResponseMessage() {
        return responseMessage;
    }

    /**
     * Establece el mensaje de respuesta.
     * 
     * @param responseMessage El mensaje de respuesta.
     */
    public void setResponseMessage(String responseMessage) {
        this.responseMessage = responseMessage;
    }
}
