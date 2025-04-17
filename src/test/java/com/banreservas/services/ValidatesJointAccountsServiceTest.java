package com.banreservas.services;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import com.banreservas.dtos.inbound.RequestDto;
import com.banreservas.dtos.inbound.ResponseValidatesJointAccountsDto;
import com.banreservas.services.implementations.ValidatesJointAccountsService;
import com.brrd.service.services.IValidarRequest;
import com.brrd.service.services.ServiceResponse;
import com.brrd.service.services.ValidarRequest;

import io.quarkus.test.junit.QuarkusTest;
import io.smallrye.mutiny.Uni;
import jakarta.ws.rs.core.MultivaluedHashMap;
import jakarta.ws.rs.core.MultivaluedMap;

/**
 *
 * @author Ing. Victor Diaz - vjdiaz@banreservas.com
 * @since 28-01-2025
 * @version 1.0
 */

@QuarkusTest
public class ValidatesJointAccountsServiceTest {

    @InjectMocks
    private ValidatesJointAccountsService service;

    @Mock
    private IValidarRequest validarRequest;

    private RequestDto mockRequest;
    private MultivaluedMap<String, String> mockHeaders;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);

        // Mock del RequestDto
        mockRequest = new RequestDto("0987654321", "1234567890");

        // Mock de Headers
        mockHeaders = new MultivaluedHashMap<>();
        mockHeaders.putSingle("id_consumidor", "testChannel");
        mockHeaders.putSingle("fecha_hora", "2025-01-28T10:15:30");
        mockHeaders.putSingle("operacion", "testOperation");
        mockHeaders.putSingle("terminal", "testTerminal");
        mockHeaders.putSingle("usuario", "testUser");
    }

    @Test
    void testBuildValidarRequest() {

        // Act
        ValidarRequest validarRequest = new ValidarRequest();

        validarRequest.setChannel(mockHeaders.getFirst("id_consumidor"));
        validarRequest.setDate(mockHeaders.getFirst("fecha_hora"));
        validarRequest.setOperationName(mockHeaders.getFirst("operacion"));
        validarRequest.setTerminal(mockHeaders.getFirst("terminal"));
        validarRequest.setUser(mockHeaders.getFirst("usuario"));
        validarRequest.setCuentaOrigen(mockRequest.originAccount());
        validarRequest.setCuentaDestino(mockRequest.destinyAccount());

        // Assert
        assertEquals("testChannel", validarRequest.getChannel());
        assertEquals("2025-01-28T10:15:30", validarRequest.getDate());
        assertEquals("testOperation", validarRequest.getOperationName());
        assertEquals("testTerminal", validarRequest.getTerminal());
        assertEquals("testUser", validarRequest.getUser());
        assertEquals("1234567890", validarRequest.getCuentaOrigen());
        assertEquals("0987654321", validarRequest.getCuentaDestino());
    }

    @Test
    void validateAccounts_ProductIsValid_Return_True() throws Exception {
        // Mock de la respuesta del cliente SOAP
        ServiceResponse mockSoapResponse = new ServiceResponse();
        mockSoapResponse.setErrorCode("000");
        mockSoapResponse.setXMLReresponse(
                "<ProductoValidos><ProductoValido><Resultado>TRUE</Resultado></ProductoValido></ProductoValidos>");

        when(validarRequest.validar(any())).thenReturn(mockSoapResponse);

        // Ejecutar el método del servicio
        Uni<ResponseValidatesJointAccountsDto> responseUni = service.validateAccounts(mockRequest, mockHeaders);

        // Verificar la respuesta
        ResponseValidatesJointAccountsDto response = responseUni.await().indefinitely();

        assertNotNull(response);
        assertTrue(response.isValidProduct());

        // Verificar que el cliente SOAP fue invocado una vez
        verify(validarRequest, times(1)).validar(any());
    }

    @Test
    void validateAccounts_Product_IsNotValid_Return_False() throws Exception {
        // Mock de la respuesta del cliente SOAP
        ServiceResponse mockSoapResponse = new ServiceResponse();
        mockSoapResponse.setErrorCode("000");
        mockSoapResponse.setXMLReresponse(
                "<ProductoValidos><ProductoValido><Resultado>FALSE</Resultado></ProductoValido></ProductoValidos>");

        when(validarRequest.validar(any())).thenReturn(mockSoapResponse);

        // Ejecutar el método del servicio
        Uni<ResponseValidatesJointAccountsDto> responseUni = service.validateAccounts(mockRequest, mockHeaders);

        // Verificar la respuesta
        ResponseValidatesJointAccountsDto response = responseUni.await().indefinitely();

        assertNotNull(response);
        assertFalse(response.isValidProduct());

        // Verificar que el cliente SOAP fue invocado una vez
        verify(validarRequest, times(1)).validar(any());
    }

    @Test
    void validateAccounts_ErrorFromSoap() throws Exception {
        // Mock de la respuesta con error del cliente SOAP
        ServiceResponse mockSoapResponse = new ServiceResponse();
        mockSoapResponse.setErrorCode("999");
        mockSoapResponse.setErrorMessage("Error interno en el backend");

        when(validarRequest.validar(any())).thenReturn(mockSoapResponse);

        // Ejecutar el método del servicio
        Uni<ResponseValidatesJointAccountsDto> responseUni = service.validateAccounts(mockRequest,
                mockHeaders);

        // Verificar que lanza una excepción
        RuntimeException exception = assertThrows(RuntimeException.class, () -> {
            responseUni.await().indefinitely();
        });

        assertEquals("Error interno en el backend", exception.getMessage());

        // Verificar que el cliente SOAP fue invocado una vez
        verify(validarRequest, times(1)).validar(any());
    }
}
