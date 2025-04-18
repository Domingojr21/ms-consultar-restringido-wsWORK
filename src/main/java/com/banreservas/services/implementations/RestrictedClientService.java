package com.banreservas.services.implementations;

import com.banreservas.dtos.inbound.RequestDto;
import com.banreservas.dtos.inbound.ResponseDto;
import com.banreservas.dtos.inbound.ResponseHeaderDto;
import com.banreservas.dtos.inbound.RestrictedClientResponseBodyDto;
import com.banreservas.dtos.outbound.VerifyRestrictedResponseDto;
import com.banreservas.services.contracts.IRestrictedClientService;
import com.banreservas.utils.BuildJsonConstructLogAppender;
import com.banreservas.utils.defaults.CodeMessages;

import io.quarkus.logging.Log;
import io.smallrye.mutiny.Uni;
import io.smallrye.mutiny.infrastructure.Infrastructure;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.ws.rs.core.MultivaluedMap;
import jakarta.ws.rs.core.Response;
import jakarta.xml.bind.JAXB;
import jakarta.xml.soap.MessageFactory;
import jakarta.xml.soap.SOAPBody;
import jakarta.xml.soap.SOAPConnection;
import jakarta.xml.soap.SOAPConnectionFactory;
import jakarta.xml.soap.SOAPElement;
import jakarta.xml.soap.SOAPEnvelope;
import jakarta.xml.soap.SOAPMessage;
import jakarta.xml.soap.SOAPPart;

import org.eclipse.microprofile.config.inject.ConfigProperty;
import org.eclipse.microprofile.faulttolerance.CircuitBreaker;
import org.eclipse.microprofile.faulttolerance.Retry;

import java.io.ByteArrayOutputStream;
import java.io.StringReader;

/**
 * Implementación del servicio para verificar clientes restringidos
 *
 * @author Ing. Domingo Ruiz - c-djruiz@banreservas.com
 * @since 14-04-2025
 * @version 1.0
 */
@ApplicationScoped
public class RestrictedClientService implements IRestrictedClientService {

    @ConfigProperty(name = "webservice.url")
    String webServiceUrl;

    @ConfigProperty(name = "webservice.soapaction")
    String soapAction;

    @Override
    @Retry(maxRetries = 3, delay = 500, retryOn = { RuntimeException.class })
    @CircuitBreaker(requestVolumeThreshold = 4, failureRatio = 0.5, delay = 1000)
    public Uni<ResponseDto> verifyRestrictedClient(RequestDto request, MultivaluedMap<String, String> headers) {
        Log.infov("Verificando cliente restringido: {0}", BuildJsonConstructLogAppender.buildJson(request));

        return Uni.createFrom().item(() -> {
            try {
                // Crear solicitud SOAP
                SOAPMessage soapRequest = createSoapRequest(request, headers);
                
                // Llamar al servicio SOAP
                SOAPMessage soapResponse = callSoapService(soapRequest);
                
                // Procesar la respuesta SOAP
                VerifyRestrictedResponseDto responseDto = processSoapResponse(soapResponse);
                
                // Construir respuesta final
                return buildFinalResponse(responseDto);
                
            } catch (Exception e) {
                Log.errorf("Error al verificar cliente restringido: %s", e.getMessage());
                throw new RuntimeException("Error al verificar cliente restringido: " + e.getMessage());
            }
        }).runSubscriptionOn(Infrastructure.getDefaultExecutor());
    }

    /**
     * Crea una solicitud SOAP para verificar cliente restringido
     */
    private SOAPMessage createSoapRequest(RequestDto request, MultivaluedMap<String, String> headers) throws Exception {
        MessageFactory messageFactory = MessageFactory.newInstance();
        SOAPMessage soapMessage = messageFactory.createMessage();
        SOAPPart soapPart = soapMessage.getSOAPPart();
        
        // SOAP Envelope
        SOAPEnvelope envelope = soapPart.getEnvelope();
        envelope.addNamespaceDeclaration("NS1", "http://intranet.banreservas.com/services/");
        
        // SOAP Body
        SOAPBody soapBody = envelope.getBody();
        SOAPElement verifyElement = soapBody.addChildElement("VerificarRestringido", "NS1");
        SOAPElement requestElement = verifyElement.addChildElement("request", "NS1");
        
        // Info
        SOAPElement infoElement = requestElement.addChildElement("Info", "NS1");
        infoElement.addChildElement("Aplicacion", "NS1").addTextNode(headers.getFirst("id_consumidor"));
        infoElement.addChildElement("Usuario", "NS1").addTextNode(headers.getFirst("usuario"));
        infoElement.addChildElement("IP", "NS1").addTextNode(headers.getFirst("terminal"));
        
        // Identificación y Tipo - usando los métodos de acceso de los records
        requestElement.addChildElement("Identificacion", "NS1").addTextNode(request.identificationNumber());
        requestElement.addChildElement("Tipo", "NS1").addTextNode(request.identificationType());
        
        soapMessage.saveChanges();
        
        Log.debugv("Solicitud SOAP creada: {0}", soapMessageToString(soapMessage));
        
        return soapMessage;
    }

    /**
     * Convierte un mensaje SOAP a String para logging
     */
    private String soapMessageToString(SOAPMessage soapMessage) throws Exception {
        ByteArrayOutputStream out = new ByteArrayOutputStream();
        soapMessage.writeTo(out);
        return new String(out.toByteArray());
    }

    /**
     * Llama al servicio SOAP con la solicitud construida
     */
    private SOAPMessage callSoapService(SOAPMessage soapRequest) throws Exception {
        SOAPConnectionFactory soapConnectionFactory = SOAPConnectionFactory.newInstance();
        SOAPConnection soapConnection = soapConnectionFactory.createConnection();
        
        java.net.URL url = new java.net.URL(webServiceUrl);
        SOAPMessage soapResponse = soapConnection.call(soapRequest, url);
        soapConnection.close();
        
        Log.debugv("Respuesta SOAP recibida: {0}", soapMessageToString(soapResponse));
        
        return soapResponse;
    }

    /**
     * Procesa la respuesta SOAP y la convierte a DTO
     */
    private VerifyRestrictedResponseDto processSoapResponse(SOAPMessage soapResponse) throws Exception {
        ByteArrayOutputStream out = new ByteArrayOutputStream();
        soapResponse.writeTo(out);
        String xmlResponse = new String(out.toByteArray());
        
        // Extraer solo el cuerpo de la respuesta SOAP (simplificado)
        String bodyContent = xmlResponse.substring(
                xmlResponse.indexOf("<VerificarRestringidoResponse>"),
                xmlResponse.indexOf("</VerificarRestringidoResponse>") + "</VerificarRestringidoResponse>".length());
        
        // Usar JAXB para convertir XML a objeto
        VerifyRestrictedResponseDto responseDto = JAXB.unmarshal(
                new StringReader(bodyContent), 
                VerifyRestrictedResponseDto.class);
        
        return responseDto;
    }

    /**
     * Construye la respuesta final del servicio
     */
    private ResponseDto buildFinalResponse(VerifyRestrictedResponseDto soapResponse) {
        ResponseHeaderDto header = new ResponseHeaderDto(
                Response.Status.OK.getStatusCode(),
                CodeMessages.MESSAGE_SUCCESS);
        
        // Usando el método de acceso correcto para los records
        boolean isRestricted = soapResponse.result().isRestricted();
        
        RestrictedClientResponseBodyDto body = null;
        if (isRestricted) {
            body = new RestrictedClientResponseBodyDto(
                    true,
                    "Restringidos",
                    "Interna");
        } else {
            body = new RestrictedClientResponseBodyDto(
                    false,
                    null,
                    null);
        }
        
        return new ResponseDto(header, body);
    }
}