package com.banreservas.utils.defaults;

import java.util.List;

/**
 * Clase que define las constantes utilizadas para los encabezados HTTP en las
 * solicitudes.
 * Esta clase contiene las constantes correspondientes a los encabezados comunes
 * que se utilizan
 * en las solicitudes HTTP dentro de la aplicación, tales como identificadores
 * de sesión, consumidor,
 * usuario, entre otros. También proporciona una lista con todos los encabezados
 * definidos.
 * 
 * @author Ing. Victor Diaz - vjdiaz@banreservas.com
 * @since 06-02-2025
 * @version 1.0
 */
public class RequestHeaders {

    /**
     * Nombre del encabezado que representa el ID de sesión.
     */
    public static final String SESSION_ID = "sessionId";

    /**
     * Nombre del encabezado que representa el ID del consumidor.
     */
    public static final String ID_CONSUMIDOR = "id_consumidor";

    /**
     * Nombre del encabezado que representa al usuario de la solicitud.
     */
    public static final String USUARIO = "usuario";

    /**
     * Nombre del encabezado que representa la fecha y hora de la solicitud.
     */
    public static final String FECHA_HORA = "fecha_hora";

    /**
     * Nombre del encabezado que representa el terminal desde el cual se realiza la
     * solicitud.
     */
    public static final String TERMINAL = "terminal";

    /**
     * Nombre del encabezado que representa la operación asociada a la solicitud.
     */
    public static final String OPERACION = "operacion";

    /**
     * Lista de todos los encabezados definidos en esta clase.
     * 
     * @see #SESSION_ID
     * @see #ID_CONSUMIDOR
     * @see #USUARIO
     * @see #FECHA_HORA
     * @see #TERMINAL
     * @see #OPERACION
     */
    public static final List<String> HEADERS = List.of(
            SESSION_ID, ID_CONSUMIDOR, USUARIO, FECHA_HORA, TERMINAL, OPERACION);
}
