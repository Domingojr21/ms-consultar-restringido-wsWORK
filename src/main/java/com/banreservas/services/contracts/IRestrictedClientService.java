package com.banreservas.services.contracts;

import com.banreservas.dtos.inbound.RequestDto;
import com.banreservas.dtos.inbound.ResponseDto;

import io.smallrye.mutiny.Uni;
import jakarta.ws.rs.core.MultivaluedMap;

/**
 * Interfaz para el servicio de verificación de clientes restringidos
 *
 * @author Ing. Domingo Ruiz - c-djruiz@banreservas.com
 * @since 14-04-2025
 * @version 1.0
 */
public interface IRestrictedClientService {
    
    /**
     * Verifica si un cliente está en la lista de restringidos
     *
     * @param request Datos del cliente a verificar
     * @param headers Cabeceras de la petición HTTP
     * @return Uni con la respuesta de la verificación
     */
    Uni<ResponseDto> verifyRestrictedClient(RequestDto request, 
                                            MultivaluedMap<String, String> headers);
}