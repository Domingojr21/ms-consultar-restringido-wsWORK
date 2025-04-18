package com.banreservas.resources;

import com.banreservas.dtos.inbound.RequestDto;
import com.banreservas.services.contracts.IRestrictedClientService;
import com.banreservas.utils.defaults.RequestHeadersValidator;

import io.quarkus.logging.Log;
import io.smallrye.mutiny.Uni;
import jakarta.inject.Inject;
import jakarta.validation.Valid;
import jakarta.ws.rs.Consumes;
import jakarta.ws.rs.POST;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.Produces;
import jakarta.ws.rs.core.Context;
import jakarta.ws.rs.core.HttpHeaders;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.MultivaluedMap;
import jakarta.ws.rs.core.Response;

/**
 * Recurso REST para la verificación de clientes restringidos
 *
 * @author Ing. Domingo Ruiz - c-djruiz@banreservas.com
 * @since 14-04-2025
 * @version 1.0
 */
@Path("/api/v1/cliente-restringido")
public class RestrictedClientResource {

    @Inject
    IRestrictedClientService restrictedClientService;

    /**
     * Endpoint para verificar si un cliente está restringido
     * 
     * @param requestDto Datos del cliente a verificar
     * @param httpHeaders Cabeceras HTTP de la solicitud
     * @return Respuesta con el resultado de la verificación
     */
    @POST
    @Path("/verificar")
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public Uni<Response> verifyRestrictedClient(@Valid RequestDto requestDto, @Context HttpHeaders httpHeaders) {
        Log.infov("Recibida solicitud para verificar cliente: {0}", requestDto);
        
        String headersValidation = RequestHeadersValidator.validateRequestHeaders(httpHeaders);
        if (!RequestHeadersValidator.VALID.equals(headersValidation)) {
            return Uni.createFrom().item(Response.status(Response.Status.BAD_REQUEST)
                    .entity(new com.banreservas.utils.defaults.ErrorResponse(headersValidation))
                    .build());
        }
        
        MultivaluedMap<String, String> headers = httpHeaders.getRequestHeaders();
        
        return restrictedClientService.verifyRestrictedClient(requestDto, headers)
                .onItem().transform(responseDto -> {
                    return Response.status(Response.Status.OK)
                            .entity(responseDto)
                            .header("id_consumidor", headers.getFirst("id_consumidor"))
                            .header("usuario", headers.getFirst("usuario"))
                            .header("fecha_hora", headers.getFirst("fecha_hora"))
                            .header("terminal", headers.getFirst("terminal"))
                            .header("operacion", headers.getFirst("operacion"))
                            .header("sessionId", headers.getFirst("sessionId"))
                            .build();
                });
    }
}