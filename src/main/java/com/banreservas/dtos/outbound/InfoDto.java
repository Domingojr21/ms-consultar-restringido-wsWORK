// package com.banreservas.dtos.outbound;

// import io.quarkus.runtime.annotations.RegisterForReflection;
// import jakarta.xml.bind.annotation.XmlAccessType;
// import jakarta.xml.bind.annotation.XmlAccessorType;
// import jakarta.xml.bind.annotation.XmlElement;
// import java.io.Serializable;

// /**
//  * DTO para la información de la aplicación, usuario y terminal
//  *
//  * @author Ing. Domingo Ruiz - c-djruiz@banreservas.com
//  * @since 14-04-2025
//  * @version 1.0
//  */

// @RegisterForReflection
// @XmlAccessorType(XmlAccessType.FIELD)
// public record InfoDto(
//     @XmlElement(name = "Aplicacion") String application,
//     @XmlElement(name = "Usuario") String user,
//     @XmlElement(name = "IP") String ip) implements Serializable {
// }