package com.banreservas.dtos.inbound;

import io.quarkus.runtime.annotations.RegisterForReflection;

/**
 * DTO (Data Transfer Object) que representa una respuesta con un encabezado y
 * un cuerpo.
 * Esta clase contiene dos propiedades:
 * - {@link #header}: Un objeto de tipo {@link ResponseHeaderDto} que representa
 * el encabezado de la respuesta.
 * - {@link #body}: Un objeto de tipo {@link AddressCreditCardResponseDto} que
 * representa el cuerpo de la respuesta.
 * 
 * La clase está registrada para reflexión a través de la anotación
 * {@link RegisterForReflection}
 * para permitir la serialización y deserialización con Quarkus.
 * 
 * Se proporcionan dos constructores:
 * - Un constructor que acepta tanto el encabezado como el cuerpo.
 * - Un constructor que solo acepta el encabezado.
 * 
 * @author Ing. Domingo Ruiz - c-djruiz@banreservas.com
 * @since 14-04-2025
 * @version 1.0
 */

@RegisterForReflection
public class ResponseDto {

    private ResponseHeaderDto header;
    private RestrictedClientResponseBodyDto body;

    /**
     * Constructor que acepta tanto el encabezado como el cuerpo de la respuesta.
     *
     * @param header El encabezado de la respuesta.
     * @param body   El cuerpo de la respuesta.
     */
    public ResponseDto(ResponseHeaderDto header, RestrictedClientResponseBodyDto body) {
        this.header = header;
        this.body = body;
    }

    /**
     * Constructor que solo acepta el encabezado de la respuesta.
     *
     * @param header El encabezado de la respuesta.
     */
    public ResponseDto(ResponseHeaderDto header) {
        this.header = header;
    }

    /**
     * Obtiene el encabezado de la respuesta.
     *
     * @return El encabezado de la respuesta.
     */
    public ResponseHeaderDto getHeader() {
        return header;
    }

    /**
     * Obtiene el cuerpo de la respuesta.
     *
     * @return El cuerpo de la respuesta, o {@code null} si no está presente.
     */
    public RestrictedClientResponseBodyDto getBody() {
        return body;
    }
}
