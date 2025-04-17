package com.banreservas.dtos.inbound;

import java.io.Serializable;
import io.quarkus.runtime.annotations.RegisterForReflection;

/**
 *
 * @author Ing. Domingo Ruiz - c-djruiz@banreservas.com
 * @since 14-04-2025
 * @version 1.0
 */

@RegisterForReflection
public record RestrictedClientResponseBodyDto(
    boolean isRestricted,
    String listName,
    String listOrigin) implements Serializable {
}
