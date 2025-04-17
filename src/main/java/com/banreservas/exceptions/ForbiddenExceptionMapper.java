package com.banreservas.exceptions;

import org.jboss.logging.Logger;

import com.banreservas.utils.defaults.CodeMessages;
import com.banreservas.utils.defaults.ErrorResponse;

import io.quarkus.security.ForbiddenException;
import jakarta.ws.rs.container.ContainerRequestContext;
import jakarta.ws.rs.core.Context;
import jakarta.ws.rs.core.Response;
import jakarta.ws.rs.ext.ExceptionMapper;
import jakarta.ws.rs.ext.Provider;

@Provider
public class ForbiddenExceptionMapper implements ExceptionMapper<ForbiddenException> {

    @Context
    private ContainerRequestContext requestContext;

    private static final Logger LOG = Logger.getLogger(ForbiddenExceptionMapper.class);

    @Override
    public Response toResponse(ForbiddenException e) {
        LOG.error(CodeMessages.MESSAGE_FORBIDDEN);
        ErrorResponse errorResponse = new ErrorResponse(CodeMessages.MESSAGE_FORBIDDEN);
        return Response.status(Response.Status.FORBIDDEN)
                .header("sessionId", requestContext.getHeaders().getFirst("sessionId"))
                .entity(errorResponse)
                .build();
    }
}
