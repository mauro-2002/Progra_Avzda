package org.openapitools.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;
import jakarta.persistence.*;
import org.openapitools.model.enums.Rol;
import org.openapitools.model.enums.StatusUsuario;
import org.springframework.data.mongodb.core.mapping.DBRef;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.mapping.Field;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;

@Getter
@Setter
@Builder
@AllArgsConstructor
@Table
@Document("usuarios")
public class Usuario {

    @Id
    private Long id;
    @Field ("NOMBRE")
    private String nombre;
    @Field ("EMAIL")
    private String email;
    private String ciudad;
    private String direccion;
    @Field("TELEFONO")
    private String telefono;
    private String password;
    private Rol rol;

    @Field("ESTADO_USUARIO")
    private StatusUsuario status;
    private String codigoActivacion;
    private LocalDateTime expiracionCodigo;

    @DBRef
    private List<Reporte> reportes;
    @DBRef
    private List<Notificacion> notificaciones;
    @DBRef
    private List<Categoria> preferencias;
}
