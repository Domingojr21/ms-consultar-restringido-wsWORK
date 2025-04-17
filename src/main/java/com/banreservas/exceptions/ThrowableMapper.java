package com.banreservas.exceptions;

import org.jboss.logging.Logger;

import com.banreservas.utils.defaults.CodeMessages;
import com.banreservas.utils.defaults.ErrorResponse;

import jakarta.ws.rs.container.ContainerRequestContext;
import jakarta.ws.rs.core.Context;
import jakarta.ws.rs.core.Response;
import jakarta.ws.rs.ext.ExceptionMapper;
import jakarta.ws.rs.ext.Provider;

@Provider
public class ThrowableMapper implements ExceptionMapper<Throwable> {

    @Context
    private ContainerRequestContext requestContext;

    private static final Logger LOG = Logger.getLogger(ThrowableMapper.class);

    @Override
    public Response toResponse(Throwable e) {
        LOG.error(e.getMessage());
        LOG.error(e.getLocalizedMessage());
        ErrorResponse errorResponse = new ErrorResponse(CodeMessages.MESSAGE_INTERNAL_SERVER_ERROR);
        return Response.status(Response.Status.INTERNAL_SERVER_ERROR)
                .header("sessionId", requestContext.getHeaders().getFirst("sessionId"))
                .entity(errorResponse).build();
    }

}
