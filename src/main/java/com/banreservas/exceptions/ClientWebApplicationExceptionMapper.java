package com.banreservas.exceptions;

import com.banreservas.utils.defaults.CodeMessages;
import com.banreservas.utils.defaults.ErrorResponse;

import jakarta.ws.rs.container.ContainerRequestContext;
import jakarta.ws.rs.core.Context;
import jakarta.ws.rs.core.Response;
import jakarta.ws.rs.ext.ExceptionMapper;
import jakarta.ws.rs.ext.Provider;

import org.jboss.logging.Logger;
import org.jboss.resteasy.reactive.ClientWebApplicationException;

@Provider
public class ClientWebApplicationExceptionMapper implements ExceptionMapper<ClientWebApplicationException> {

    @Context
    private ContainerRequestContext requestContext;

    private static final Logger LOG = Logger.getLogger(ClientWebApplicationExceptionMapper.class);

    @Override
    public Response toResponse(ClientWebApplicationException e) {
        LOG.error(e.getMessage());
        if (e.getMessage().contains("400")) {
            ErrorResponse errorResponse = new ErrorResponse(CodeMessages.MESSAGE_BAD_REQUEST);
            return Response.status(Response.Status.BAD_REQUEST)
                    .header("sessionId", requestContext.getHeaders().getFirst("sessionId"))
                    .entity(errorResponse)
                    .build();
        } else {
            ErrorResponse errorResponse = new ErrorResponse(CodeMessages.MESSAGE_INTERNAL_SERVER_ERROR);
            return Response.status(Response.Status.INTERNAL_SERVER_ERROR)
                    .header("sessionId", requestContext.getHeaders().getFirst("sessionId"))
                    .entity(errorResponse).build();
        }
    }
}
