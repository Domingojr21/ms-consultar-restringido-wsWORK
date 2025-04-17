package com.banreservas.exceptions;

import org.jboss.logging.Logger;

import com.banreservas.utils.defaults.CodeMessages;
import com.banreservas.utils.defaults.ErrorResponse;

import io.quarkus.security.UnauthorizedException;
import jakarta.ws.rs.container.ContainerRequestContext;
import jakarta.ws.rs.core.Context;
import jakarta.ws.rs.core.Response;
import jakarta.ws.rs.ext.ExceptionMapper;
import jakarta.ws.rs.ext.Provider;

@Provider
public class UnauthorizedExceptionMapper implements ExceptionMapper<UnauthorizedException> {

    @Context
    private ContainerRequestContext requestContext;

    private static final Logger LOG = Logger.getLogger(UnauthorizedExceptionMapper.class);

    @Override
    public Response toResponse(UnauthorizedException e) {
        LOG.error(CodeMessages.MESSAGE_UNAUTHORIZED);
        ErrorResponse errorResponse = new ErrorResponse(CodeMessages.MESSAGE_UNAUTHORIZED);
        return Response.status(Response.Status.UNAUTHORIZED)
                .header("sessionId", requestContext.getHeaders().getFirst("sessionId")).entity(errorResponse)
                .build();
    }
}
