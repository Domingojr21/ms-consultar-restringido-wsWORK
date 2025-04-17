package com.banreservas.dtos.outbound;

import io.quarkus.runtime.annotations.RegisterForReflection;
import jakarta.xml.bind.annotation.XmlAccessType;
import jakarta.xml.bind.annotation.XmlAccessorType;
import jakarta.xml.bind.annotation.XmlElement;
import jakarta.xml.bind.annotation.XmlRootElement;
import java.io.Serializable;

/**
 * DTO para la respuesta del servicio web de verificación de clientes restringidos
 *
 * @author Ing. Domingo Ruiz - c-djruiz@banreservas.com
 * @since 14-04-2025
 * @version 1.0
 */
@RegisterForReflection
@XmlRootElement(name = "VerificarRestringidoResponse")
@XmlAccessorType(XmlAccessType.FIELD)
public record VerifyRestrictedResponseDto(
    @XmlElement(name = "VerificarRestringidoResult") VerifyRestrictedResultDto result) implements Serializable {
}
