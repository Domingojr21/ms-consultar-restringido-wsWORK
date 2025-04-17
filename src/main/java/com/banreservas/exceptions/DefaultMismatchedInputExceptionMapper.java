package com.banreservas.exceptions;

import org.jboss.logging.Logger;

import com.banreservas.utils.defaults.CodeMessages;
import com.banreservas.utils.defaults.ErrorResponse;
import com.fasterxml.jackson.databind.exc.MismatchedInputException;

import jakarta.ws.rs.container.ContainerRequestContext;
import jakarta.ws.rs.core.Context;
import jakarta.ws.rs.core.Response;
import jakarta.ws.rs.ext.ExceptionMapper;
import jakarta.ws.rs.ext.Provider;

@Provider
public class DefaultMismatchedInputExceptionMapper implements ExceptionMapper<MismatchedInputException> {

    @Context
    private ContainerRequestContext requestContext;

    private static final Logger LOG = Logger.getLogger(DefaultMismatchedInputExceptionMapper.class);

    @Override
    public Response toResponse(MismatchedInputException e) {
        LOG.error(CodeMessages.MESSAGE_BAD_REQUEST);
        ErrorResponse errorResponse = new ErrorResponse(CodeMessages.MESSAGE_BAD_REQUEST);
        return Response.status(Response.Status.BAD_REQUEST)
                .header("sessionId", requestContext.getHeaders().getFirst("sessionId")).entity(errorResponse)
                .build();
    }
}