package com.banreservas.exceptions;

import jakarta.ws.rs.container.ContainerRequestContext;
import jakarta.ws.rs.core.Context;
import jakarta.ws.rs.core.Response;
import jakarta.ws.rs.ext.ExceptionMapper;
import jakarta.ws.rs.ext.Provider;

import org.jboss.logging.Logger;

import com.banreservas.utils.defaults.ErrorResponse;

@Provider
public class IllegalArgumentExceptionMapper implements ExceptionMapper<IllegalArgumentException> {

    @Context
    private ContainerRequestContext requestContext;

    private static final Logger LOG = Logger.getLogger(IllegalArgumentExceptionMapper.class);

    @Override
    public Response toResponse(IllegalArgumentException e) {
        LOG.error(e.getMessage());
        ErrorResponse errorResponse = new ErrorResponse(e.getMessage());
        return Response.status(Response.Status.BAD_REQUEST)
                .header("sessionId", requestContext.getHeaders().getFirst("sessionId")).entity(errorResponse)
                .build();
    }
}
