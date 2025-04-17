package com.banreservas.exceptions;

import jakarta.ws.rs.BadRequestException;
import jakarta.ws.rs.container.ContainerRequestContext;
import jakarta.ws.rs.core.Context;
import jakarta.ws.rs.core.Response;
import jakarta.ws.rs.ext.ExceptionMapper;
import jakarta.ws.rs.ext.Provider;

import org.jboss.logging.Logger;

import com.banreservas.utils.defaults.CodeMessages;
import com.banreservas.utils.defaults.ErrorResponse;

@Provider
public class BadRequestExceptionMapper implements ExceptionMapper<BadRequestException> {

    @Context
    private ContainerRequestContext requestContext;

    private static final Logger LOG = Logger.getLogger(BadRequestExceptionMapper.class);

    @Override
    public Response toResponse(BadRequestException e) {
        LOG.error(e.getMessage());
        String error = e.getMessage() != null ? e.getMessage() : CodeMessages.MESSAGE_BAD_REQUEST;
        ErrorResponse errorResponse = new ErrorResponse(error);
        return Response.status(Response.Status.BAD_REQUEST)
                .header("sessionId", requestContext.getHeaders().getFirst("sessionId"))
                .entity(errorResponse)
                .build();
    }
}