package com.banreservas.exceptions;

import jakarta.ws.rs.WebApplicationException;
import jakarta.ws.rs.container.ContainerRequestContext;
import jakarta.ws.rs.core.Context;
import jakarta.ws.rs.core.Response;
import jakarta.ws.rs.ext.ExceptionMapper;
import jakarta.ws.rs.ext.Provider;

import org.jboss.logging.Logger;

import com.banreservas.dtos.inbound.ResponseDto;
import com.banreservas.dtos.inbound.ResponseHeaderDto;
import com.banreservas.utils.defaults.CodeMessages;

@Provider
public class WebApplicationExceptionMapper implements ExceptionMapper<WebApplicationException> {

    @Context
    private ContainerRequestContext requestContext;

    private static final Logger LOG = Logger.getLogger(WebApplicationExceptionMapper.class);

    @Override
    public Response toResponse(WebApplicationException e) {
        LOG.error(e.getMessage());
        return Response.status(Response.Status.INTERNAL_SERVER_ERROR)
                .header("sessionId", requestContext.getHeaders().getFirst("sessionId"))
                .entity(new ResponseDto(
                        new ResponseHeaderDto(Response.Status.INTERNAL_SERVER_ERROR.getStatusCode(),
                                CodeMessages.MESSAGE_INTERNAL_SERVER_ERROR),
                        null))
                .build();
    }
}
