package com.banreservas.resources;

import static io.restassured.RestAssured.given;
import java.util.HashMap;
import java.util.Map;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import com.banreservas.dtos.inbound.RequestDto;
import com.banreservas.services.implementations.ValidatesJointAccountsService;
import com.banreservas.utils.defaults.CodeMessages;

import io.quarkus.test.junit.QuarkusTest;
import io.quarkus.test.security.TestSecurity;
import io.restassured.http.ContentType;
import jakarta.inject.Inject;
import static org.hamcrest.Matchers.equalTo;

/**
 *
 * @author Ing. Victor Diaz - vjdiaz@banreservas.com
 * @since 28-01-2025
 * @version 1.0
 */

@QuarkusTest
public class ValidatesJointAccountsResourceTest {

    @Inject
    ValidatesJointAccountsService validaCuentasMancomunadasService;

    private RequestDto requestDto;
    private Map<String, String> headers;

    @BeforeEach
    void setup() {
        requestDto = new RequestDto("1234567890", "0987654321");

        headers = new HashMap<>();
        headers.put("id_consumidor", "test-consumer");
        headers.put("usuario", "test-user");
        headers.put("fecha_hora", "2025-01-28T12:00:00");
        headers.put("terminal", "test-terminal");
        headers.put("operacion", "test-operation");
        headers.put("sessionId", "test-session");
    }

    @Test
    @TestSecurity(authorizationEnabled = false)
    void testValidateAccountsSuccess() throws Exception {

        // Llamada al endpoint
        given()
                .contentType(ContentType.JSON)
                .header("id_consumidor", headers.get("id_consumidor"))
                .header("usuario", headers.get("usuario"))
                .header("fecha_hora", headers.get("fecha_hora"))
                .header("terminal", headers.get("terminal"))
                .header("operacion", headers.get("operacion"))
                .header("sessionId", headers.get("sessionId"))
                .body(requestDto)
                .when()
                .post("/v1/valida-cuentas-mancomunadas")
                .then()
                .statusCode(200)
                .contentType(ContentType.JSON)
                .header("id_consumidor", headers.get("id_consumidor"))
                .body("header.responseMessage", equalTo("Exitoso"))
                .body("body.isValidProduct", equalTo(true));
    }

    @Test
    void testValidateAccountsAuthorization() throws Exception {

        // Llamada al endpoint
        given()
                .contentType(ContentType.JSON)
                .header("id_consumidor", headers.get("id_consumidor"))
                .header("usuario", headers.get("usuario"))
                .header("fecha_hora", headers.get("fecha_hora"))
                .header("terminal", headers.get("terminal"))
                .header("operacion", headers.get("operacion"))
                .header("sessionId", headers.get("sessionId"))
                .body(requestDto)
                .when()
                .post("/v1/valida-cuentas-mancomunadas")
                .then()
                .statusCode(401)
                .contentType(ContentType.JSON)
                .body("message", equalTo(CodeMessages.MESSAGE_UNAUTHORIZED));
    }

    @Test
    @TestSecurity(authorizationEnabled = false)
    void testValidateAccounts_MissingHeader() {
        // Ejecuta la prueba sin un encabezado obligatorio
        given()
                .contentType(ContentType.JSON)
                .header("id_consumidor", headers.get("id_consumidor"))
                .header("usuario", headers.get("usuario"))
                .header("fecha_hora", headers.get("fecha_hora"))
                .header("terminal", headers.get("terminal"))
                .header("operacion", headers.get("operacion"))
                .body(requestDto)
                .when()
                .post("/v1/valida-cuentas-mancomunadas")
                .then()
                .statusCode(400) // Código esperado para una solicitud incorrecta
                .body("header.responseMessage", equalTo("Header sessionId es obligatorio"));
    }
}
