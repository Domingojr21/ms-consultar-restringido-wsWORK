package com.banreservas.dtos.outbound;

import io.quarkus.runtime.annotations.RegisterForReflection;
import jakarta.xml.bind.annotation.XmlAccessType;
import jakarta.xml.bind.annotation.XmlAccessorType;
import jakarta.xml.bind.annotation.XmlElement;
import java.io.Serializable;

/**
 * DTO para la información del resultado de la verificación
 *
 * @author Ing. Domingo Ruiz - c-djruiz@banreservas.com
 * @since 14-04-2025
 * @version 1.0
 */
@RegisterForReflection
@XmlAccessorType(XmlAccessType.FIELD)
public record ResultInfoDto(
    @XmlElement(name = "OK") boolean ok) implements Serializable {
}
