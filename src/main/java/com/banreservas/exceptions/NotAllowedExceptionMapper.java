package com.banreservas.exceptions;

import jakarta.ws.rs.NotAllowedException;
import jakarta.ws.rs.container.ContainerRequestContext;
import jakarta.ws.rs.core.Context;
import jakarta.ws.rs.core.Response;
import jakarta.ws.rs.ext.ExceptionMapper;
import jakarta.ws.rs.ext.Provider;

import org.jboss.logging.Logger;

import com.banreservas.utils.defaults.CodeMessages;
import com.banreservas.utils.defaults.ErrorResponse;

@Provider
public class NotAllowedExceptionMapper implements ExceptionMapper<NotAllowedException> {

    @Context
    private ContainerRequestContext requestContext;

    private static final Logger LOG = Logger.getLogger(NotAllowedExceptionMapper.class);

    public Response toResponse(NotAllowedException e) {
        LOG.error(e.getMessage());
        ErrorResponse errorResponse = new ErrorResponse(CodeMessages.MESSAGE_FORBIDDEN);
        return Response.status(Response.Status.METHOD_NOT_ALLOWED)
                .header("sessionId", requestContext.getHeaders().getFirst("sessionId"))
                .entity(errorResponse).build();
    }
}
