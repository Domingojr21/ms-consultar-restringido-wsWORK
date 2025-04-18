package com.banreservas.resources;

import com.banreservas.dtos.inbound.RequestDto;
import com.banreservas.dtos.inbound.ResponseDto;
import com.banreservas.dtos.inbound.ResponseHeaderDto;
import com.banreservas.services.contracts.IRestrictedClientService;
import com.banreservas.utils.defaults.RequestHeadersValidator;

import io.quarkus.logging.Log;
import io.quarkus.security.Authenticated;
import io.smallrye.mutiny.Uni;
import jakarta.annotation.security.RolesAllowed;
import jakarta.enterprise.context.ApplicationScoped;
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
@Path("/v1")
@Authenticated
@ApplicationScoped
public class RestrictedClientResource {

    @Inject
    IRestrictedClientService restrictedClientService;

    @POST
    @Path("/cliente-restringido/verificar")
    @RolesAllowed("consultar-cliente-restringido")
    @Produces(MediaType.APPLICATION_JSON)
    @Consumes(MediaType.APPLICATION_JSON)
    public Uni<Response> verifyRestrictedClient(@Valid RequestDto requestDto, @Context HttpHeaders httpHeaders) {
        Log.infov("Recibida solicitud para verificar cliente: {0}", requestDto);
        
        // Validación de los encabezados de la solicitud
        String responseHeaderValidation = RequestHeadersValidator.validateRequestHeaders(httpHeaders);
        if (!responseHeaderValidation.equalsIgnoreCase("valid")) {
            return Uni.createFrom().item(Response.status(Response.Status.BAD_REQUEST)
                    .entity(new ResponseDto(
                            new ResponseHeaderDto(Response.Status.BAD_REQUEST.getStatusCode(), 
                                    responseHeaderValidation),
                            null))
                    .build());
        }

        MultivaluedMap<String, String> headers = httpHeaders.getRequestHeaders();

        // Llamada reactiva al servicio
        return restrictedClientService.verifyRestrictedClient(requestDto, headers)
                .onItem().transform(responseDto -> {
                    // Construir la respuesta cuando el resultado esté disponible
                    return Response.ok(responseDto)
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