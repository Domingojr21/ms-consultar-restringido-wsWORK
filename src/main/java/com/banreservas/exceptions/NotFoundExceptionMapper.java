package com.banreservas.exceptions;

import org.jboss.logging.Logger;

import com.banreservas.dtos.inbound.ResponseDto;
import com.banreservas.dtos.inbound.ResponseHeaderDto;
import com.banreservas.utils.defaults.ErrorResponse;

import jakarta.ws.rs.NotFoundException;
import jakarta.ws.rs.container.ContainerRequestContext;
import jakarta.ws.rs.core.Context;
import jakarta.ws.rs.core.Response;
import jakarta.ws.rs.ext.ExceptionMapper;
import jakarta.ws.rs.ext.Provider;

@Provider
public class NotFoundExceptionMapper implements ExceptionMapper<NotFoundException> {

    @Context
    private ContainerRequestContext requestContext;

    private static final Logger LOG = Logger.getLogger(NotFoundExceptionMapper.class);

    @Override
    public Response toResponse(NotFoundException e) {
        LOG.error(e.getMessage());
        ErrorResponse errorResponse = new ErrorResponse(e.getMessage());
        return Response.status(Response.Status.NOT_FOUND)
                .header("sessionId", requestContext.getHeaders().getFirst("sessionId"))
                .entity(new ResponseDto(
                        new ResponseHeaderDto(Response.Status.NOT_FOUND.getStatusCode(), errorResponse.getMessage()),
                        null))
                .build();
    }
}
