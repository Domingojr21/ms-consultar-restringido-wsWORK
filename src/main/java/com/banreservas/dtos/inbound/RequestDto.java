package com.banreservas.dtos.inbound;

import com.fasterxml.jackson.annotation.JsonProperty;
import io.quarkus.runtime.annotations.RegisterForReflection;
import jakarta.validation.constraints.NotEmpty;

/**
 *
 * @author Ing. Domingo Ruiz - c-djruiz@banreservas.com
 * @since 14-04-2025
 * @version 1.0
 */

@RegisterForReflection
public record RequestDto(
        @JsonProperty("identificacionNumero") @NotEmpty(message = "identificationNumber required") String identificationNumber,
        @JsonProperty("identificacionTipo") @NotEmpty(message = "identificationType required") String identificationType) {
}