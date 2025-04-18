package com.banreservas.services.implementations;

import org.eclipse.microprofile.faulttolerance.CircuitBreaker;
import org.eclipse.microprofile.faulttolerance.Retry;
import com.banreservas.dtos.inbound.RequestDto;
import com.banreservas.dtos.inbound.ResponseDto;
import com.banreservas.dtos.inbound.ResponseHeaderDto;
import com.banreservas.dtos.inbound.RestrictedClientResponseBodyDto;
import com.banreservas.services.contracts.IRestrictedClientService;
import com.banreservas.utils.BuildJsonConstructLogAppender;
import com.banreservas.utils.defaults.CodeMessages;

// Importaciones para el cliente SOAP (asumiendo que se generaron correctamente)
import com.banreservas.intranet.services.ClienteRequest;
import com.banreservas.intranet.services.ClientesServiceSoap;
import com.banreservas.intranet.services.RestringidoResponse;
import com.banreservas.intranet.services.ServiceRequestInfo;
import com.banreservas.intranet.services.TipoIdentificacion;

import io.quarkiverse.cxf.annotation.CXFClient;
import io.quarkus.logging.Log;
import io.smallrye.mutiny.Uni;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import jakarta.ws.rs.core.MultivaluedMap;
import jakarta.ws.rs.core.Response;

/**
 * Implementación del servicio para verificar clientes restringidos
 *
 * @author Ing. Domingo Ruiz - c-djruiz@banreservas.com
 * @since 14-04-2025
 * @version 1.0
 */
@ApplicationScoped
public class RestrictedClientService implements IRestrictedClientService {

    @Inject
    @CXFClient("consultarRestringido")
    private ClientesServiceSoap clientesServiceSoap;

    @Override
    @Retry(maxRetries = 3, delay = 500, retryOn = {
            RuntimeException.class
    })
    @CircuitBreaker(requestVolumeThreshold = 4, failureRatio = 0.5, delay = 1000)
    public Uni<ResponseDto> verifyRestrictedClient(RequestDto request,
            MultivaluedMap<String, String> headers) {

        Log.infov("Verificando cliente restringido: {0}", BuildJsonConstructLogAppender.buildJson(request));

        try {
            // Preparar la solicitud para el servicio SOAP
            ClienteRequest clienteRequest = new ClienteRequest();
            ServiceRequestInfo requestInfo = new ServiceRequestInfo();
            requestInfo.setAplicacion(headers.getFirst("id_consumidor"));
            requestInfo.setUsuario(headers.getFirst("usuario"));
            requestInfo.setIP(headers.getFirst("terminal"));
            clienteRequest.setInfo(requestInfo);
            
            // Configurar identificación y tipo
            clienteRequest.setIdentificacion(request.identificationNumber());
            
            // Mapear el tipo de identificación
            TipoIdentificacion tipoId;
            switch (request.identificationType().toUpperCase()) {
                case "CEDULA":
                    tipoId = TipoIdentificacion.CEDULA;
                    break;
                case "RNC":
                    tipoId = TipoIdentificacion.RNC;
                    break;
                case "PASAPORTE":
                    tipoId = TipoIdentificacion.PASAPORTE;
                    break;
                default:
                    tipoId = TipoIdentificacion.CEDULA;
            }
            clienteRequest.setTipo(tipoId);
            
            // Invocar el servicio SOAP
            RestringidoResponse soapResponse = clientesServiceSoap.verificarRestringido(clienteRequest);
            
            // Construir y retornar la respuesta
            return Uni.createFrom().item(
                new ResponseDto(
                    new ResponseHeaderDto(Response.Status.OK.getStatusCode(), CodeMessages.MESSAGE_SUCCESS),
                    new RestrictedClientResponseBodyDto(
                        soapResponse.isEsRestringido(),
                        soapResponse.isEsRestringido() ? "Lista de Restringidos" : null,
                        soapResponse.isEsRestringido() ? "Interna" : null
                    )
                )
            );
            
        } catch (Exception e) {
            Log.errorf("Error al verificar cliente restringido: %s", e.getMessage());
            throw new RuntimeException("Error al verificar cliente restringido: " + e.getMessage());
        }
    }
}