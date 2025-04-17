package com.banreservas.resources;

import com.banreservas.dtos.inbound.RequestDto;
import com.banreservas.dtos.inbound.ResponseValidatesJointAccountsDto;
import com.banreservas.services.implementations.ValidatesJointAccountsService;

import io.quarkus.test.Mock;
import io.smallrye.mutiny.Uni;
import jakarta.ws.rs.core.MultivaluedMap;

/**
 *
 * @author Ing. Victor Diaz - vjdiaz@banreservas.com
 * @since 28-01-2025
 * @version 1.0
 */

@Mock
public class ValidatesJointAccountsServiceMock extends ValidatesJointAccountsService {

    @Override
    public Uni<ResponseValidatesJointAccountsDto> validateAccounts(RequestDto request,
            MultivaluedMap<String, String> headers) {

        // Simula la respuesta del servicio
        ResponseValidatesJointAccountsDto mockResponse = new ResponseValidatesJointAccountsDto(true);

        return Uni.createFrom().item(mockResponse);
    }

}
